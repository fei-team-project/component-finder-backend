package org.example;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;
import org.json.JSONArray;

public class GPTCategorySelector {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("OPENAI_API_KEY");
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static void main(String[] args) {
        //String userRequest = "Give me a part with this parameters MKHJ\n";
        //String userRequest = "Find me a part that has similar parts than this part  MKHJ\n";
        //String userRequest = "Describe me this part MKHJ\n";
        String userRequest = "what are complementary parts to NCP1234";
        //String userRequest = "What is the part simmilar to this one MKHJ\n";
        //String userRequest = "I don't have part MKHJ what else can i use\n";
        //String userRequest = "What is the price of this part MKJH\n";

        int category = determineCategory(userRequest);
        System.out.println("We have selected the category " + category);
    }

    public static int determineCategory(String text) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonInput = new JSONObject();
            jsonInput.put("model", "gpt-4");
            // chatgpt-4o-latest#
            // gpt-4
            // gpt-3.5-turbo-0125

            JSONArray messages = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", "Categorize this text into one of the categories:\n"
                    + "1. Search for a part by parameters.\n"
                    + "2. Suggest complementary parts.\n"
                    + "3. Find documentation for the part.\n"
                    + "4. Find similar parts.\n"
                    + "5. Find out the availability and price of the part.\n"
                    + "Text: \"" + text + "\"\n"
                    + "Category:");
            messages.put(userMessage);
            jsonInput.put("messages", messages);
            jsonInput.put("max_tokens", 10);

            // request
            OutputStream os = connection.getOutputStream();
            os.write(jsonInput.toString().getBytes());
            os.flush();
            os.close();

            // response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // parsovanie response
            JSONObject jsonResponse = new JSONObject(response.toString());
            String categoryText = jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content").trim();

            // dostat z response cislo kategorie
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(categoryText);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            } else {
                System.out.println("No valid category number found in the response.");
            }

        } catch (Exception e) {
            System.out.println("An error occurred when calling the GPT API: " + e.getMessage());
        }
        return -1; // V pripade chyby
    }
}
