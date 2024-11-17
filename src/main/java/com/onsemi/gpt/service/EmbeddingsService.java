package com.onsemi.gpt.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import io.github.cdimascio.dotenv.Dotenv;

//import com.onsemi.gpt.models.GPTFiltersRequest;
//import com.onsemi.gpt.models.GPTRequest;
//import com.onsemi.gpt.models.GPTResponse;
import org.json.*;

public class EmbeddingsService {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String OPENAI_API_KEY = dotenv.get("OPENAI_API_KEY");

    public static class Category {
        String title;
        List<String> examples;

        public Category(String title, List<String> examples) {
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
            hash.append(String.format("%02x", b));
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
            categoryEmbeddings.put(category.title, embeddings);
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

    public static String classifyPrompt(String userPrompt, Map<String, List<double[]>> categoryEmbeddings) throws Exception {
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

        return bestCategory;
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

        categories.add(new Category(
                "Find Documentation on Part",
                Arrays.asList(
                        "Please provide documentation for N channel SiC MOSFETs in a D2PAK7 package, with a breakdown voltage above 650 V and a drain current capacity of at least 60 A",
                        "Could you share documentation for SiC transistors that can withstand 100A and have a breakdown voltage exceeding 1200 V, ideally with packages that include separate pins for gate driver and power bus source?",
                        "Please provide datasheets or technical documentation for N channel MOSFETs in the mid breakdown voltage range of 50-100 V, particularly dual n-type packaged products supporting drain currents between 10-40 A.",
                        "I need documentation on dual n-type MOSFETs with a common drain, focusing on parts with Vbr around 20-40 V and a drain current of at least 8 A.",
                        "Could you provide documentation for p channel MOSFET transistors with automotive qualification, especially those optimized for the best RdsON x Qg parameters?",
                        "Please send the technical documentation for N channel MOSFETs with the best figure of merit in the breakdown voltage range of 100-150 V.",
                        "Can you provide documentation for an IGBT suitable for automotive applications, with breakdown voltage between 600-800 V and saturation voltage below 2 V?",
                        "Please provide datasheets or technical documentation for 3.3V automotive-rated diodes capable of at least 1W power dissipation."
                )
        ));
        categories.add(new Category(
                "Suggest Complementary Parts",
                Arrays.asList(
                        "Can you suggest complementary components for an N channel SiC MOSFET in D2PAK7 package, with a breakdown voltage above 650 V and drain current of at least 60 A?",
                        "Could you recommend suitable gate driver circuits or power management components for SiC transistors that can withstand 100A and over 1200 breakdown voltage, ideally with separate pins for gate driver and power bus source?",
                        "Please suggest complementary parts for mid breakdown voltage range 50-100 V N channel MOSFETs, preferably dual n-type packaged products supporting drain currents between 10-40 A.",
                        "I’m searching for complementary components for dual n-type MOSFETs with common drain. Please focus on parts that support Vbr around 20-40 V and drain current of at least 8 A.",
                        "Could you recommend complementary parts for p channel MOSFET transistors with automotive qualification, optimized for RdsON x Qg performance?",
                        "What complementary components would pair well with N channel MOSFETs with the best figure of merit for breakdown voltage in the 100-150 V range?",
                        "Please suggest complementary components to go with an IGBT for automotive applications, with breakdown voltage between 600-800 V and a saturation voltage below 2 V.",
                        "Could you recommend complementary parts for 3.3V automotive-rated diodes that can handle at least 1W power dissipation?"
                )
        ));
        categories.add(new Category(
                "Search for a Part by Parameters",
                Arrays.asList(
                        "Please give me N channel SiC MOSFETs in D2PAK7 package with breakdown voltage above 650 V and capable of delivering drain current at least 60 A.",
                        "Do you have any SiC transistors available that could withstand 100A and survive more than 1200 breakdown voltage? I am interested in packages that provide separate pins for gate driver and power bus source.",
                        "Try to state all mid breakdown voltage range 50 -100 V N channel MOSFETs. Give me only dual n-type packaged products if possible for drain currents between 10 - 40 A.",
                        "Searching dual n type MOSFETs with common drain. Please look only for Vbr around 20 – 40 V and drain current at least 8 A.",
                        "I am looking for p channel MOSFET transistors with automotive qualification, with best RdsON x Qg parameters.",
                        "Give me N MOSFETs with best figure of merit from range of breakdown voltage between 100 – 150V.",
                        "What IGBT would you propose for automotive application with breakdown voltage from 600-800 V and saturation voltage below 2 V?",
                        "Please advise 3.3V diodes automotive capable with at least 1W power dissipation."
                )
        ));

        categories.add(new Category(
                "find similar parts",
                Arrays.asList(
                        "Can you find an N channel MOSFET similar to those in the 200-300 V range, optimized for high-speed switching and low RdsON?",
                        "Are there alternative SiC MOSFETs with a package comparable to TO-247, featuring a breakdown voltage of 1200 V and a drain current of 50 A?",
                        "Could you recommend MOSFETs similar to those in a dual package with common source configuration, capable of handling 30 A and 60 V?",
                        "I'm looking for alternatives to IGBTs in the 650 V range with a saturation voltage of less than 1.8 V and high switching efficiency.",
                        "Can you find p channel MOSFETs comparable to those qualified for automotive use, focusing on 40 V breakdown voltage and low gate charge?",
                        "Are there SiC transistors with specs similar to those rated for 80 A, 1700 V, and high thermal performance in a TO-247-4L package?"
                )
        ));

        categories.add(new Category(
                "find out the availability and price of the part",
                Arrays.asList(
                        "Could you provide the availability and price for an N channel SiC MOSFET in a TO-220 package, rated for 650 V and 20 A?",
                        "Are there SiC transistors available with a breakdown voltage of 1200 V and a drain current of 100 A? Please include pricing details.",
                        "What is the cost and stock status of an automotive-qualified IGBT with a breakdown voltage of 600 V and a saturation voltage of 1.5 V?",
                        "Can you provide pricing and availability for 3.3V automotive-rated diodes capable of handling 2 W of power dissipation?",
                        "What are the current costs and stock levels for dual n-type MOSFETs with a common drain, rated for 40 V and 15 A?",
                        "Are there N channel MOSFETs in stock with a breakdown voltage of 75 V, optimized for high current handling (30 A)? What is their price?"
                )
        ));
        try {
        categoryEmbeddings = loadOrGenerateEmbeddings(categories, "embeddings.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String run(String message){
        try {
            return classifyPrompt(message, categoryEmbeddings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "okay";
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
                    new TestEntry("Can you suggest N channel MOSFETs similar to those in DPAK packaging but optimized for lower RdsON?", "find similar parts"),
                    new TestEntry("Are there MOSFETs with characteristics close to 100 V breakdown and a 25 A drain current in a dual package?", "find similar parts"),
                    new TestEntry("What is the availability and price for a 1.5 kV IGBT optimized for power switching in renewable energy systems?", "find out the availability and price of the part"),
                    new TestEntry("Could you find alternatives to 60 V dual n-type MOSFETs with common source connections?", "find similar parts"),
                    new TestEntry("What is the cost and availability of 20 V p channel MOSFETs with automotive qualification and low gate charge?", "find out the availability and price of the part"),
                    new TestEntry("Are there similar transistors to SiC MOSFETs with a 650 V breakdown and high efficiency for use in motor control?", "find similar parts"),
                    new TestEntry("Can you provide pricing details for TO-220 packaged SiC MOSFETs with a 30 A drain current and 900 V breakdown voltage?", "find out the availability and price of the part"),
                    new TestEntry("Are there MOSFETs like those rated for 40 V and 10 A, but in a common drain configuration?", "find similar parts"),
                    new TestEntry("What is the stock availability and price of 3.3 V Zener diodes capable of dissipating 1.5 W in automotive applications?", "find out the availability and price of the part"),
                    new TestEntry("Can you suggest SiC MOSFETs with a higher current rating similar to the ones with 100 A and 1200 V breakdown?", "find similar parts"),
                    new TestEntry("Could you provide the pricing and availability of high power IGBTs for industrial use with a 600 V breakdown voltage?", "find out the availability and price of the part"),
                    new TestEntry("Are there alternatives to SiC MOSFETs rated for 650 V that offer higher efficiency for DC-DC converters?", "find similar parts"),
                    new TestEntry("What is the availability and pricing for a 1200 V SiC MOSFET with a 60 A drain current capacity?", "find out the availability and price of the part"),
                    new TestEntry("Can you find similar p-channel MOSFETs that support a breakdown voltage of 20-40 V with automotive qualification?", "find similar parts"),
                    new TestEntry("What is the price and availability of N-channel MOSFETs with the best figure of merit in the 100-150V range?", "find out the availability and price of the part"),
                    new TestEntry("Are there dual n-type MOSFETs with a common drain configuration supporting 30 V breakdown and 15 A drain current?", "find similar parts"),
                    new TestEntry("What is the availability and cost for dual N-channel MOSFETs with a 40 V breakdown voltage and 8 A current in a D2PAK package?", "find out the availability and price of the part"),
                    new TestEntry("Can you find high-performance transistors for automotive applications with breakdown voltages between 100 V and 150 V?", "find similar parts"),
                    new TestEntry("Could you provide the availability and pricing for SiC MOSFETs rated for high current in a package with a gate driver pin?", "find out the availability and price of the part"),
                    new TestEntry("Can you suggest alternative p-channel MOSFETs with optimized RdsON x Qg for automotive applications?", "find similar parts"),
                    new TestEntry("Is the LM317 adjustable voltage regulator in stock?", "find out the availability and price of the part"),
                    new TestEntry("What is the price of the ATmega328P microcontroller from Microchip?", "find out the availability and price of the part"),
                    new TestEntry("What is the availability of a 100uF electrolytic capacitor with a 25V rating?", "find out the availability and price of the part"),
                    new TestEntry("How much does a 1k ohm, 1/4 watt carbon film resistor cost?", "find out the availability and price of the part"),
                    new TestEntry("Is the 555 timer IC available in DIP-8 package?", "find out the availability and price of the part"),
                    new TestEntry("What is the cost of the Texas Instruments CD74HCT00N quad 2-input NAND gate IC?", "find out the availability and price of the part"),
                    new TestEntry("How much does the Vishay 1N4148 signal diode cost?", "find out the availability and price of the part"),
                    new TestEntry("Is the Bourns 3386P-1-103LF 10k ohm potentiometer available?", "find out the availability and price of the part"),
                    new TestEntry("What is the price of the 74LS04 hex inverting logic gate IC?", "find out the availability and price of the part"),
                    new TestEntry("What is the availability of the 100nF ceramic capacitor with a 50V rating?", "find out the availability and price of the part"),
                    new TestEntry("What is the price of the Omron G5V-1-DC12 relay?", "find out the availability and price of the part"),
                    new TestEntry("Is the Vishay SMD Zener diode, 5.1V, 1W in stock?", "find out the availability and price of the part"),
                    new TestEntry("How much does the Panasonic ECJ-2FB1E104K 100nF ceramic capacitor cost?", "find out the availability and price of the part"),
                    new TestEntry("Is the 74HC595 shift register in stock?", "find out the availability and price of the part"),
                    new TestEntry("What is the cost of a 10A, 250V fast blow fuse?", "find out the availability and price of the part"),
                    new TestEntry("Is the JST XH 2.54mm 4-pin connector available?", "find out the availability and price of the part"),
                    new TestEntry("What is the price of the Vishay TL3 1W 5.1V Zener diode?", "find out the availability and price of the part"),
                    new TestEntry("What is the availability of the Murata CSTCE16M0V53-R0 crystal oscillator?", "find out the availability and price of the part"),
                    new TestEntry("What is the cost of a 10A automotive fuse for 12V systems?", "find out the availability and price of the part"),
                    new TestEntry("Is the Cree XLamp XP-E2 LED in stock?", "find out the availability and price of the part"),
                    new TestEntry("What is the price of the TE Connectivity 2-position female header connector?", "find out the availability and price of the part"),
                    new TestEntry("What are some similar parts to the LM741 operational amplifier?", "find similar parts"),
                    new TestEntry("Find me transistors similar to the 2N2222 NPN BJT.", "find similar parts"),
                    new TestEntry("What are some alternatives to the LM7805 5V voltage regulator IC?", "find similar parts"),
                    new TestEntry("I need MOSFETs similar to the IRF540 N-channel MOSFET.", "find similar parts"),
                    new TestEntry("What are some alternatives to the Atmel ATmega328P microcontroller?", "find similar parts"),
                    new TestEntry("Recommend me parts similar to the 1N4148 fast switching diode.", "find similar parts"),
                    new TestEntry("Find me parts like the 10k ohm carbon film resistor.", "find similar parts"),
                    new TestEntry("What are the alternatives to the Omron G5V-1-DC12 relay?", "find similar parts"),
                    new TestEntry("Suggest a similar part to the ALPS EC11 rotary encoder.", "find similar parts"),
                    new TestEntry("What are some alternatives to the MCP602 dual operational amplifier?", "find similar parts"),
                    new TestEntry("Find capacitors like the Panasonic 10uF 50V ceramic capacitor.", "find similar parts"),
                    new TestEntry("What are the alternatives to the 74LS00 quad 2-input NAND gate IC?", "find similar parts"),
                    new TestEntry("I need transistors similar to the 2N3904 NPN transistor.", "find similar parts"),
                    new TestEntry("What are some alternatives to the Panasonic ERJ-6ENF1002V 10k ohm resistor?", "find similar parts"),
                    new TestEntry("Recommend me alternatives to the STMicroelectronics STM32F103C8T6 microcontroller.", "find similar parts"),
                    new TestEntry("Find similar diodes to the 1N4007 general-purpose rectifier diode.", "find similar parts"),
                    new TestEntry("What are the alternatives to the Vishay BC846B NPN transistor?", "find similar parts"),
                    new TestEntry("Find me similar parts to the 10W 12V automotive fuse.", "find similar parts"),
                    new TestEntry("What are some alternatives to the Texas Instruments TPS7A02 low-noise linear regulator?", "find similar parts"),
                    new TestEntry("Recommend me similar parts to the Murata CSTCE16M0V53-R0 crystal oscillator.", "find similar parts"),
                    new TestEntry("What are some similar switches to the Cherry MX mechanical switch?", "find similar parts"),
                    new TestEntry("Where can I find the datasheet for the LM317 adjustable voltage regulator?", "Find Documentation on Part"),
                    new TestEntry("Can you provide the pinout diagram for the 555 timer IC?", "Find Documentation on Part"),
                    new TestEntry("Where can I find the programming guide for the ATmega328P microcontroller?", "Find Documentation on Part"),
                    new TestEntry("What should I connect to PIN 1 of the ATmega328P microcontroller?", "Find Documentation on Part"),
                    new TestEntry("What is the maximum voltage for the LM741 operational amplifier?", "Find Documentation on Part"),
                    new TestEntry("Tell me more about the 2N2222 NPN BJT transistor.", "Find Documentation on Part"),
                    new TestEntry("Where can I find the datasheet for the 1N4148 fast switching diode?", "Find Documentation on Part"),
                    new TestEntry("Can you provide the user manual for the ESP32-WROOM-32 microcontroller?", "Find Documentation on Part"),
                    new TestEntry("What is the application note for the LM7805 5V voltage regulator?", "Find Documentation on Part"),
                    new TestEntry("Where can I find the reference design for the TL072 low-noise JFET op-amp?", "Find Documentation on Part"),
                    new TestEntry("What is the schematic for a circuit using the BC547 NPN transistor?", "Find Documentation on Part"),
                    new TestEntry("Can you provide the block diagram for the STM32F103C8T6 microcontroller?", "Find Documentation on Part"),
                    new TestEntry("Where can I find the specification sheet for the 10k ohm carbon film resistor?", "Find Documentation on Part"),
                    new TestEntry("Can you provide the technical document for the Panasonic 10uF 50V ceramic capacitor?", "Find Documentation on Part"),
                    new TestEntry("Where can I find the installation guide for the Omron G5V-1-DC12 relay?", "Find Documentation on Part"),
                    new TestEntry("What is the wiring diagram for the Cherry MX mechanical switch?", "Find Documentation on Part"),
                    new TestEntry("Where can I find the test report for the 1N4007 general-purpose rectifier diode?", "Find Documentation on Part"),
                    new TestEntry("Can you provide the evaluation board documentation for the STM32F103C8T6 microcontroller?", "Find Documentation on Part"),
                    new TestEntry("Where can I find the design guide for the MeanWell HDR-15-12 power supply?", "Find Documentation on Part"),
                    new TestEntry("Where can I find the datasheet for a 10k ohm resistor?", "Find Documentation on Part"),
                    new TestEntry("Can you provide the user manual for the Texas Instruments TPL5010 low-power microcontroller?", "Find Documentation on Part"),
                    new TestEntry("What resistor should I use with the IRF540N N-channel MOSFET?", "Suggest Complementary Parts"),
                    new TestEntry("What capacitor is suitable for the LM7805 voltage regulator?", "Suggest Complementary Parts"),
                    new TestEntry("Can you recommend a diode for the 555 timer IC circuit?", "Suggest Complementary Parts"),
                    new TestEntry("I want to use an ATmega328P microcontroller with this, which one do you recommend?", "Suggest Complementary Parts"),
                    new TestEntry("What is the best op-amp for the audio amplifier application using the NE5532?", "Suggest Complementary Parts"),
                    new TestEntry("Which part do you recommend for the LM317 adjustable voltage regulator?", "Suggest Complementary Parts"),
                    new TestEntry("What goes well with the 1N4148 fast switching diode?", "Suggest Complementary Parts"),
                    new TestEntry("What should I use with the Arduino Uno microcontroller?", "Suggest Complementary Parts"),
                    new TestEntry("What inductor should I use with the 10uF ceramic capacitor?", "Suggest Complementary Parts"),
                    new TestEntry("What transistor should I use with the Omron G5V-1-DC12 relay?", "Suggest Complementary Parts"),
                    new TestEntry("Can you recommend a zener diode for the LM7812 voltage regulator?", "Suggest Complementary Parts"),
                    new TestEntry("What thermistor should I use with the ESP32-WROOM-32 microcontroller?", "Suggest Complementary Parts"),
                    new TestEntry("What potentiometer should I use with the TL072 op-amp?", "Suggest Complementary Parts"),
                    new TestEntry("What crystal oscillator should I use with the STM32F103C8T6 microcontroller?", "Suggest Complementary Parts"),
                    new TestEntry("What fuse should I use with the MeanWell HDR-15-12 power supply?", "Suggest Complementary Parts"),
                    new TestEntry("What switch should I use with the Omron G5V-1-DC12 relay?", "Suggest Complementary Parts"),
                    new TestEntry("What connector should I use with the Raspberry Pi 4 Model B?", "Suggest Complementary Parts"),
                    new TestEntry("What LED should I use with the 1k ohm resistor?", "Suggest Complementary Parts"),
                    new TestEntry("What resistor should I use with the 5mm red LED?", "Suggest Complementary Parts"),
                    new TestEntry("What capacitor should I use with the OP07 op-amp?", "Suggest Complementary Parts"),
                    new TestEntry("Give me a MOSFET with a voltage rating of 100V, like the IRLZ44N", "Search for a Part by Parameters"),
                    new TestEntry("Find a capacitor with capacitance of 100uF, such as the Panasonic EEU-FM1E101", "Search for a Part by Parameters"),
                    new TestEntry("Search for a resistor with resistance of 1k ohm, like the Yageo CFR-25JB-1000K", "Search for a Part by Parameters"),
                    new TestEntry("Find a microcontroller with 32KB of flash memory, such as the ATmega328P", "Search for a Part by Parameters"),
                    new TestEntry("Search for a voltage regulator with 5V output, such as the LM7805", "Search for a Part by Parameters"),
                    new TestEntry("Give me a part with a current rating of 1A, like the 1N5400 diode", "Search for a Part by Parameters"),
                    new TestEntry("Give me all parts for a DC-DC converter", "Search for a Part by Parameters"),
                    new TestEntry("Find a diode with a forward voltage of 0.7V, such as the 1N4148", "Search for a Part by Parameters"),
                    new TestEntry("Search for an inductor with an inductance of 10mH, like the Murata LQG15HS10NJ02", "Search for a Part by Parameters"),
                    new TestEntry("Give me a transistor with a gain of 100, such as the 2N2222", "Search for a Part by Parameters"),
                    new TestEntry("Find a Zener diode with a breakdown voltage of 5.1V, such as the 1N4728A", "Search for a Part by Parameters"),
                    new TestEntry("Search for a thermistor with a resistance of 10k ohm, like the NTC 10k thermistor from Honeywell", "Search for a Part by Parameters"),
                    new TestEntry("Give me a relay with a coil voltage of 12V, such as the Omron G5V-1-DC12", "Search for a Part by Parameters"),
                    new TestEntry("Find a potentiometer with a resistance of 10k ohm, like the Bourns 3296W-1-103LF", "Search for a Part by Parameters"),
                    new TestEntry("Search for a crystal oscillator with a frequency of 16MHz, such as the ECS-160-20-5PXN", "Search for a Part by Parameters"),
                    new TestEntry("Give me a fuse with a current rating of 2A, like the Littelfuse 212 2A", "Search for a Part by Parameters"),
                    new TestEntry("Find a switch with a voltage rating of 250V, such as the TE Connectivity 1-1433701-2", "Search for a Part by Parameters"),
                    new TestEntry("Search for a connector with 10 pins, like the Molex 10-02-7104", "Search for a Part by Parameters"),
                    new TestEntry("Give me an LED with a forward current of 20mA, such as the Cree XP-E2", "Search for a Part by Parameters")
            ));

            int correct = 0;
            for (int i = 0; i < testEntries.size(); i++) {
                TestEntry entry = testEntries.get(i);
                String result = run(entry.prompt);
                System.out.println(i + "/" + testEntries.size() + " Expected: " + entry.expectedCategory + ", Actual: " + result);
                if (result.equals(entry.expectedCategory)) {
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