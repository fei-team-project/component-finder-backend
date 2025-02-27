package com.onsemi.gpt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onsemi.gpt.models.GPTRequest;
import com.onsemi.gpt.models.GPTResponse;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class GPTSimilarPartsService {
    private final ChatGPTAPI chatGPTAPI;

    public GPTResponse<?> getResponse(GPTRequest request) throws Exception {
        String prompt = "I need to select the ID of product from this request and category this request. The ID is a continuous string without spaces," +
                " consisting of both letters and numbers, with a minimum length of 6. Return the string from this request. " +
                "If there isn't an id return empty string."
                + "Request: " + request.getRequest().replace("\"", "");

        String gptResponse = chatGPTAPI.generateResponseWithModel(
                prompt,
                "You have to select product id from this request. Answer only with the ID nothing else.",
                "gpt-4-1106-preview"
        );

        JSONObject jsonResponse = new JSONObject(gptResponse);
        String selectedProductId = jsonResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content").trim();

//        String searchLink = selectedProductId.isBlank()
//                ? String.format("https://www.onsemi.com/design/tools-software/product-recommendation-tools-plus/api/similarProducts?generalPartNumber=%s", selectedProductId)
//                : String.format("https://www.onsemi.com/design/tools-software/product-recommendation-tools-plus/api/similarProductsByOpn?orderablePartNumber=%s", selectedProductId);

//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest httpRequest = HttpRequest.newBuilder()
//                .uri(URI.create())
//                .GET()
//                .build();
//
//        HttpResponse<String> httpResponse;
//        GPTResponse<?> response = new GPTResponse<>();
//
//        try {
//            httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
//            response.setContent(httpResponse.body());
//        } catch (Exception e) {
//            response.setContent("Hľadanie neúspešné. Skús svoju požiadavku sformulovať inak.");
//        }

        HttpClient client = HttpClient.newHttpClient();

        String requestBody = """
        {
            "sourceParams": [
                {
                    "sourceType": "WPN",
                    "source": "fcd4n60"
                },
                {
                    "sourceType": "CATEGORY",
                    "source": "mosfets"
                }
            ]
        }
        """;

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://www.onsemi.com/design/tools-software/ymbi/api/recommend"))
                .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response code: " + httpResponse.statusCode());
        System.out.println("Response body: " + httpResponse.body());

        // Inicializácia Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Parsovanie JSON do JsonNode
        JsonNode rootNode = objectMapper.readTree(httpResponse.body());

        // Uloženie výsledkov do zoznamu
        List<String> extractedItems = new ArrayList<>();

        // Iterácia cez pole JSON objektov
        for (JsonNode item : rootNode) {
            JsonNode recommendationResult = item.get("recommendationResult");
            if (recommendationResult != null) {
                JsonNode recommendationResults = recommendationResult.get("recommendationResults");
                if (recommendationResults != null && recommendationResults.isArray()) {
                    for (JsonNode recommendation : recommendationResults) {
                        String heading = recommendation.get("heading").asText();
                        String description = recommendation.get("description").asText();
                        extractedItems.add("Heading: " + heading + ", Description: " + description);
                    }
                }
            }
        }

        extractedItems.forEach(System.out::println);

        String joinedItems = String.join("\n\n", extractedItems);

        GPTResponse<?> response = new GPTResponse<>();
        response.setContent(joinedItems.replaceAll("<[^>]+>", ""));
        return response;
    }
}
