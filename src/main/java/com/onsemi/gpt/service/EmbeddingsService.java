package com.onsemi.gpt.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import io.github.cdimascio.dotenv.Dotenv;

import org.json.*;

public class EmbeddingsService {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String OPENAI_API_KEY = dotenv.get("OPENAI_API_KEY");

    public static class Category {
        int id;
        String title;
        List<String> examples;

        public Category(String title, List<String> examples, int id) {
            this.id = id;
            this.title = title;
            this.examples = examples;
        }
    }

    public static JSONObject getEmbedding(String text) throws Exception {
        String apiUrl = "https://api.openai.com/v1/embeddings";
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + OPENAI_API_KEY);
        connection.setDoOutput(true);

        JSONObject payload = new JSONObject();
        payload.put("input", text);
        payload.put("model", "text-embedding-3-large");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            JSONObject resJson = new JSONObject(response.toString());
            int usedTokens = resJson.getJSONObject("usage").getInt("total_tokens");
            System.out.println("Tokens consumed: " + usedTokens);
            System.out.println("For prompt: " + text);
            return resJson;
        }
    }

    public static String computeHash(List<Category> categories) throws Exception {
        JSONObject json = new JSONObject();
        for (Category category : categories) {
            json.put(category.title, category.examples);
        }
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hashBytes = digest.digest(json.toString().getBytes(StandardCharsets.UTF_8));
        StringBuilder hash = new StringBuilder();
        for (byte b : hashBytes) {
            hash.append(b);
        }
        return hash.toString();
    }

    public static Map<String, List<double[]>> loadOrGenerateEmbeddings(List<Category> categories, String filename) throws Exception {
        String currentHash = computeHash(categories);

        File file = new File(filename);
        if (file.exists()) {
            String content = java.nio.file.Files.readString(file.toPath());
            JSONObject data = new JSONObject(content);
            if (data.getString("hash").equals(currentHash)) {
                System.out.println("Categories were not changed, thus loaded from file.");
                return parseEmbeddings(data.getJSONObject("embeddings"));
            }
        }

        System.out.println("Categories were changed. Generating embeddings for categories...");
        Map<String, List<double[]>> categoryEmbeddings = new HashMap<>();

        for (Category category : categories) {
            List<double[]> embeddings = new ArrayList<>();
            for (String example : category.examples) {
                JSONObject response = getEmbedding(example);
                JSONArray embeddingArray = response.getJSONArray("data").getJSONObject(0).getJSONArray("embedding");
                double[] embedding = new double[embeddingArray.length()];
                for (int i = 0; i < embeddingArray.length(); i++) {
                    embedding[i] = embeddingArray.getDouble(i);
                }
                embeddings.add(embedding);
            }
//            categoryEmbeddings.put(category.title, embeddings);

            // 2. pristup
            // kazda kategoria bude 1 vector cize budeme mat 5 vektorov

            double[] averagedEmbedding = new double[embeddings.get(0).length];
            for (double[] embedding : embeddings) {
                for (int i = 0; i < embedding.length; i++) {
                    averagedEmbedding[i] += embedding[i];
                }
            }
            for (int i = 0; i < averagedEmbedding.length; i++) {
                averagedEmbedding[i] /= embeddings.size();
            }
            categoryEmbeddings.put(category.title, Collections.singletonList(averagedEmbedding));
        }

        JSONObject saveData = new JSONObject();
        saveData.put("hash", currentHash);
        saveData.put("embeddings", categoryEmbeddings);

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(saveData.toString());
        }

        return categoryEmbeddings;
    }

    private static Map<String, List<double[]>> parseEmbeddings(JSONObject embeddingsData) {
        Map<String, List<double[]>> embeddings = new HashMap<>();
        for (String category : embeddingsData.keySet()) {
            JSONArray categoryEmbeddings = embeddingsData.getJSONArray(category);
            List<double[]> embeddingList = new ArrayList<>();
            for (int i = 0; i < categoryEmbeddings.length(); i++) {
                JSONArray embeddingArray = categoryEmbeddings.getJSONArray(i);
                double[] embedding = new double[embeddingArray.length()];
                for (int j = 0; j < embeddingArray.length(); j++) {
                    embedding[j] = embeddingArray.getDouble(j);
                }
                embeddingList.add(embedding);
            }
            embeddings.put(category, embeddingList);
        }
        return embeddings;
    }

    public static int classifyPrompt(String userPrompt, Map<String, List<double[]>> categoryEmbeddings) throws Exception {
        JSONObject response = getEmbedding(userPrompt);
        JSONArray userEmbeddingArray = response.getJSONArray("data").getJSONObject(0).getJSONArray("embedding");
        double[] userEmbedding = new double[userEmbeddingArray.length()];
        for (int i = 0; i < userEmbeddingArray.length(); i++) {
            userEmbedding[i] = userEmbeddingArray.getDouble(i);
        }

        String bestCategory = null;
        double highestSimilarity = Double.NEGATIVE_INFINITY;

        for (Map.Entry<String, List<double[]>> entry : categoryEmbeddings.entrySet()) {
            String category = entry.getKey();
            for (double[] embedding : entry.getValue()) {
                double similarity = cosineSimilarity(userEmbedding, embedding);
                if (similarity > highestSimilarity) {
                    highestSimilarity = similarity;
                    bestCategory = category;
                }
            }
        }

        assert bestCategory != null;
        return Integer.parseInt(bestCategory);
    }

    public static double cosineSimilarity(double[] a, double[] b) {
        double dotProduct = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
            normA += Math.pow(a[i], 2);
            normB += Math.pow(b[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    static List<Category> categories = new ArrayList<>();
    static Map<String, List<double[]>> categoryEmbeddings;
    static {

        // -------------
        // TRAINING DATA
        // -------------

        categories.add(new Category(
//                "Search for a Part by Parameters",
                "1",
                Arrays.asList(
                        "Find a MOSFET with a voltage rating of 600V, a current capacity of 30A, and Rds(on) less than 50mohm.",
                        "Search for a low-dropout regulator with a maximum output voltage of 5V, input voltage up to 12V, and a minimum efficiency of 85%.",
                        "Do you have a rectifier diode that supports 1000V, 10A, and a reverse recovery time under 35ns?",
                        "Show me operational amplifiers with a gain bandwidth product over 10 MHz, input bias current below 10nA, and rail-to-rail output.",
                        "What switching regulators can output 3.3V, handle 2A, and operate at a frequency of at least 1 MHz?",
                        "Find a comparator with a response time faster than 100 ns, a supply voltage of 5V, and a push-pull output stage.",
                        "Do you have IGBTs with a voltage rating of 1200V, current capacity of 50A, and a switching frequency above 20 kHz?",
                        "Find a Schottky diode with a forward voltage drop under 0.4V, a reverse voltage of 40V, and a current capacity of 5A.",
                        "Search for a DC-DC converter with an input range of 9V to 36V, an output of 12V, and efficiency greater than 90%.",
                        "Find a thermistor with a resistance of 10kohm at 25C, a beta coefficient of 3950K, and a tolerance of 1%.",
                        "What RF amplifiers offer a gain of at least 20 dB at 2.4 GHz, with a noise figure under 1.5 dB?",
                        "Search for a temperature sensor with IC communication, an operating range of -40C to 125C, and a resolution of 0.1C.",
                        "Find a triac that operates at 800V and handles 40A.",
                        "Show me sensors compatible with a 3.3V supply.",
                        "What Zener diodes offer a breakdown voltage of exactly 3.3V?"
                ),
                1
        ));

        categories.add(new Category(
//                "Suggest Complementary Parts",
                "2",
                Arrays.asList(
                        "Find compatible components for part number NVTYS006N06CL.",
                        "What parts work well with NVMFS003P03P8Z?",
                        "Suggest matching parts for NVMFS6H836NL.",
                        "Identify complementary components for the part NVMFS5C673NL.",
                        "Can you find suitable counterparts for NSV60200DMTWTBG?",
                        "Look up supplementary parts for NSVT45010MW6T1G.",
                        "What would pair well with SBC847BWT1G?",
                        "Search for associated parts for SBC847BPDW1T1G.",
                        "Find adjunct components to complement ARX383CSSM00SMD20.",
                        "Propose compatible parts for NFVA22512NP2T.",
                        "Can you recommend related parts for NFVA23512NP2T?",
                        "Look for parts that match NFAM2512L7B.",
                        "Suggest other parts that work with NFAM5065L4BT.",
                        "Identify any compatible accessories for NFAM3065L4B.",
                        "Can you locate synergistic parts for NB3N5573DTR2G?"
                ),
                2
        ));

        categories.add(new Category(
//                "Find Documentation on Part",
                "3",
                Arrays.asList(
                        "Please provide documentation for this N channel SiC MOSFET  NVHL110N65S3HF",
                        "Could you share documentation for NVHL120N12S3HF?",
                        "Provide the datasheet for the MOSFET NVHL060N10S3F.",
                        "I need documentation for the dual n-type MOSFET NVHL030N04S3F.",
                        "Could you provide the technical specifications for PVHL050P65S3F?",
                        "Please send the datasheet for NVHL100N15S3F.",
                        "Can you provide documentation for IGHL080N08S3F?",
                        "Retrieve the technical documentation for the diode ADHL03V33D1F.",
                        "Where can I find the datasheet for NVHL110N65S3HF component?",
                        "Locate the reference materials for the SiC transistor NVHL120N12S3HF.",
                        "Search for the technical manual related to NVHL060N10S3F.",
                        "Gather documentation on the dual n-type MOSFET NVHL030N04S3F.",
                        "Access the datasheet for the P-channel MOSFET PVHL050P65S3F.",
                        "Identify the specification sheet for NVHL100N15S3F",
                        "Look up technical details for the IGBT IGHL080N08S3F."
                ),
                3
        ));

        categories.add(new Category(
//                "find similar parts",
                "4",
                Arrays.asList(
                        "Show me alternatives for NBA3N5573MNTXG",
                        "I'm looking for components like NSVMUN2236T1G. Can you help?",
                        "Are there any substitutes for NSVMUN5236T1G?",
                        "Find me similar parts to NCV7518MWATXG.",
                        "Can you suggest alternatives for NCP81080DR2G",
                        "What are the comparable components to AFGB40T65RQDN?",
                        "Please list similar components to FGHL50T65LQDT",
                        "Can you help me find alternatives for AS0149ATSC00XUEA1-DPBR?",
                        "Options like NCV333ASN2T1G",
                        "Find similar to NCV20084DR2G.",
                        "Alternatives for NVG800A75L4DSB2",
                        "Similar to NXH600N65L4Q2F2PG",
                        "Substitutes for NCV8711ASN330T1G",
                        "Find similar to NCV8768CD33ABR2G.",
                        "Alternatives for NIS6150MT1TXG"
                ),
                4
        ));

        categories.add(new Category(
//                "find out the availability and price of the part",
                "5",
                Arrays.asList(
                        "What is the price of AFGB30T65SQDN?",
                        "Can you check if FCA20N60F is in stock?",
                        "How much does the NXH450N65L4Q2cost?",
                        "At what price can I buy FQP3P50?",
                        "Is NXH600N105L7F5P2HG available?",
                        "Could you confirm the availability of FQP3P50?",
                        "What’s the current price of FCA20N60F?",
                        "Is part FCH041N65EFL4currently available?",
                        "What is the cost of the FQP3P50 component?",
                        "Is FQP3P50 in stock, and how much is it?",
                        "Could you tell me if the NXH450N65L4Q2is available?",
                        "How much would it cost to get FQP3P50?",
                        "Check availability and pricing for the part FQP3P50.",
                        "Is FQP3P50 listed in the inventory?",
                        "At what price can I purchase FQP3P50?"
                ),
                5
        ));
        try {
        categoryEmbeddings = loadOrGenerateEmbeddings(categories, "embeddings.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int run(String message){
        try {
            return classifyPrompt(message, categoryEmbeddings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static class TestEntry {
        String prompt;
        String expectedCategory;

        public TestEntry(String prompt, String expectedCategory) {
            this.prompt = prompt;
            this.expectedCategory = expectedCategory;
        }
    }

    public static double testSuccess(){
        try {

            List<TestEntry> testEntries = new ArrayList<>(Arrays.asList(
                new TestEntry("Search for voltage supervisors with a threshold of 1.8V.", "1"),
                new TestEntry("Do you have microcontrollers with at least 16 GPIO pins?", "1"),
                new TestEntry("Find an LDO regulator with a dropout voltage under 200 mV.", "1"),
                new TestEntry("What capacitive touch ICs support multi-touch functionality?", "1"),
                new TestEntry("Search for current sense amplifiers with a gain of 50 V/V.", "1"),
                new TestEntry("Find an optocoupler that supports a 5kV isolation voltage.", "1"),
                new TestEntry("Show me analog switches with an on-resistance below 50mohm.", "1"),
                new TestEntry("Search for a linear voltage regulator with an input voltage range of 2.5V to 12V.", "1"),
                new TestEntry("What voltage references provide a 1.25V output?", "1"),
                new TestEntry("Find a buck converter with an efficiency greater than 95% at 5V output.", "1"),
                new TestEntry("What motor drivers support stepper motors with 24V input?", "1"),
                new TestEntry("Do you have audio amplifiers with THD under 0.01%?", "1"),
                new TestEntry("Find a power switch that can handle 10A at 5V.", "1"),
                new TestEntry("Search for LED drivers compatible with RGB lighting applications.", "1"),
                new TestEntry("What switching regulators can output 3.3V and handle 2A?", "1"),
                new TestEntry("Can you suggest complementary electronic components that pair well with NB3N51044DTR2G?", "2"),
                new TestEntry("I’m designing a circuit using NB3H5150. Could you recommend complementary parts, such as matching MOSFETs, drivers, or resistors?", "2"),
                new TestEntry("I’m using an NB3H60113GH3MTR2G in a power switching application. What are some complementary parts or drivers that would work well in this setup?", "2"),
                new TestEntry("Please list as many complementary parts as possible for the NB3H5150MNTXG, including suitable MOSFETs, gate drivers, resistors, and diodes for common configurations.", "2"),
                new TestEntry("What parts would you recommend to complement the NCV2252SQ2T2G in a circuit?", "2"),
                new TestEntry("I’m building an H-bridge circuit using NCV2393DR2G as the high-side switch. What complementary parts should I use for the low-side and gate drivers?", "2"),
                new TestEntry("Could you recommend complementary MOSFETs and related circuitry for pairing with NCV2200SN1T1G in a half-bridge design?", "2"),
                new TestEntry("Based on the NCV2200SN2T1G datasheet, what components (drivers, resistors, diodes) would pair well with it in terms of voltage, current, and switching speed?", "2"),
                new TestEntry("I want to use the NSVMMUN2217LT1G for a power supply circuit. What complementary components would you suggest for effective operation?", "2"),
                new TestEntry("I’m new to circuit design. Could you suggest some parts to use alongside NSVMMUN2231LT1G for a basic power circuit?", "2"),
                new TestEntry("What are the typical complementary components for the NSVDTA123EM3T5G in a switching application?", "2"),
                new TestEntry("What components would complement the NSVDTC123EM3T5G in a DC motor driver circuit?", "2"),
                new TestEntry("Can you list complementary parts for NSVMUN5213DW1T3G in terms of gate drivers and protection components?", "2"),
                new TestEntry("Suggest a suitable complementary MOSFET and related circuitry for use with NSVBC124XDXV6T1G in a push-pull converter.", "2"),
                new TestEntry("I’m designing a solar charge controller and planning to use the SMUN5213DW1T1G. What complementary components, such as diodes and microcontrollers, would be suitable for this application?", "2"),
                new TestEntry("Fetch the design documentation for ADHL03V33D1F", "3"),
                new TestEntry("Could you share the technical datasheet for NVHL040N65S3F?", "3"),
                new TestEntry("Provide information about the dual n-type MOSFET NVHL025N04S3F.", "3"),
                new TestEntry("I need the documentation for SUPERFET III MOSFET NVHL095N65S3F.", "3"),
                new TestEntry("Please locate the specifications for the P-channel MOSFET PVHL040P65S3F.", "3"),
                new TestEntry("Can you fetch the datasheet for the automotive-rated IGBT IGHL060N08S3F?", "3"),
                new TestEntry("Retrieve the technical manual for the 3.3V diode ADHL05V33D1F.", "3"),
                new TestEntry("Provide the documentation for the N channel SiC MOSFET NVHL027N65S3F.", "3"),
                new TestEntry("Access the reference sheet for the MOSFET NVHL080N12S3F.", "3"),
                new TestEntry("Could you gather the design details for the dual n-type MOSFET NVHL018N04S3F?", "3"),
                new TestEntry("Please send the datasheet for the P-channel transistor PVHL030P65S3F.", "3"),
                new TestEntry("Could you provide the thermal characteristics for the MOSFET NVHL065N65S3F?", "3"),
                new TestEntry("Retrieve the gate charge curve for the transistor NVHL040N10S3F", "3"),
                new TestEntry("I need capacitance vs. reverse voltage graph for SiC diode FFSB0665A.", "3"),
                new TestEntry("Locate the maximum ratings for the IGBT IGHL080N08S3F", "3"),
                new TestEntry("Can you find the package dimensions for the diode FFSB0665A?", "3"),
                new TestEntry("I'm searching for similar parts to AS0149ATSC00XUEA1-TRBR.", "4"),
                new TestEntry("Show me options like FSB50825AB", "4"),
                new TestEntry("Find components that are similar to FSB50550BS.", "4"),
                new TestEntry("Can you provide alternatives for MMBF4391LT1G?", "4"),
                new TestEntry("I'm looking for substitutes for NSVJ6904DSB6T1G", "4"),
                new TestEntry("Are there any similar components to NVLJWD040N06CLTAG?", "4"),
                new TestEntry("Similar to NVMJD015N06CLTWG.", "4"),
                new TestEntry("Similar to FPF2496UCX.", "4"),
                new TestEntry("Find substitutes for NCP302035MNTWG", "4"),
                new TestEntry("Similar to NCP302040MNTWG.", "4"),
                new TestEntry("Options like NCV8402AMNWT1G", "4"),
                new TestEntry("Alternatives for NCV8405BDTRKG.", "4"),
                new TestEntry("Find similar to AR0522SRSC09SURA0-DP", "4"),
                new TestEntry("Substitutes for NOIP1SP0480A-STI1", "4"),
                new TestEntry("Is component FCA20N60F in stock?", "5"),
                new TestEntry("Could you confirm the stock status of AFGB30T65SQDN?", "5"),
                new TestEntry("What is the unit price of FQP3P50?", "5"),
                new TestEntry("Find out the availability FQP3P50 and its price.", "5"),
                new TestEntry("Can you provide availability details for FQP3P50?", "5"),
                new TestEntry("Is component AFGB30T65SQDN in stock? And for how much?", "5"),
                new TestEntry("Look up the pricing for FCH041N65EFL4.", "5"),
                new TestEntry("Could you find out the availability and provide a price for the FQP3P50 component?", "5"),
                new TestEntry("Do you have component NXH450N65L4Q2 in stock, and what is its current cost?", "5"),
                new TestEntry("Can you check if the FCA20N60F is in stock?", "5"),
                new TestEntry("I need to confirm whether the FCH041N65EFL4 is available and at what price.", "5"),
                new TestEntry("Is there a way to verify if AFGB30T65SQDN is in stock, along with its respective price?", "5"),
                new TestEntry("Can you help me with providing pricing details for FQP3P50.", "5"),
                new TestEntry("What is the estimated price for a single FCH041N65EFL4 unit?", "5"),
                new TestEntry("I’d like to know if the AFGH4L60T120RW-STDis available in stock.", "5")
            ));

            int correct = 0;
            for (int i = 0; i < testEntries.size(); i++) {
                TestEntry entry = testEntries.get(i);
                int result = run(entry.prompt);
                System.out.println(i + "/" + testEntries.size() + " Expected: " + entry.expectedCategory + ", Classified as: " + result);
                if (result == Integer.parseInt(entry.expectedCategory)) {
                    correct++;
                }
            }

            return (double) correct / testEntries.size() * 100;
        } catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}