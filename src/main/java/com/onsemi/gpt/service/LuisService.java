package com.onsemi.gpt.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onsemi.gpt.models.GPTRequest;
import io.github.cdimascio.dotenv.Dotenv;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LuisService {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String LUIS_API_KEY = dotenv.get("LUIS_API_KEY");
    private static final String API_URL = "https://tp1.cognitiveservices.azure.com/language/:analyze-conversations?api-version=2022-10-01-preview";

    public int getModelId(GPTRequest request) {
        try {
            String query = request.getRequest();
            String requestBody = String.format("{\"kind\":\"Conversation\",\"analysisInput\":{\"conversationItem\":{\"id\":\"1\",\"text\":\"%s\",\"modality\":\"text\",\"language\":\"en\",\"participantId\":\"1\"}},\"parameters\":{\"projectName\":\"TP_Intents\",\"verbose\":true,\"deploymentName\":\"T1_decision_model_deployment\",\"stringIndexType\":\"TextElement_V8\"}}", query);

            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", LUIS_API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.toString());

                if (jsonNode.has("result")) {
                    JsonNode resultNode = jsonNode.get("result");
                    if (resultNode.has("prediction")) {
                        JsonNode predictionNode = resultNode.get("prediction");
                        if (predictionNode.has("topIntent")) {
                            String topIntent = predictionNode.get("topIntent").asText().trim();
                            return mapIntentToId(topIntent);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int mapIntentToId(String intent) {
        switch (intent) {
            case "products_by_parameters":
                return 0;
            case "complementary_parts":
                return 1;
            case "parts_documentation":
                return 2;
            case "similar_products":
                return 3;
            case "availability_and_price_check":
                return 4;
            case "None":
                return 5;
            default:
                return -1;
        }
    }
}
