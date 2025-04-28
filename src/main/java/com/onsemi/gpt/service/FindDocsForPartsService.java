package com.onsemi.gpt.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onsemi.gpt.models.GPTResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FindDocsForPartsService {
    public GPTResponse<?> getResponse(String selectedProductId) {
        GPTResponse<?> GPTResponse = new GPTResponse<>();
        String responseContent = "For the product " + selectedProductId + ", I found these docs:";
        String similarProductsAPILink = String.format("https://www.onsemi.com/design/tools-software/product-recommendation-tools-plus/api/similarProducts?generalPartNumber=%s", selectedProductId);
        String complementaryProductsAPILink = "https://www.onsemi.com/design/tools-software/ymbi/api/recommend";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest similarProductsRequest = HttpRequest.newBuilder().uri(URI.create(
                similarProductsAPILink)).GET().build();

        try {
            HttpResponse<String> response = client.send(similarProductsRequest, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (responseBody.isBlank() == false) {
                responseContent += "\n\n• Similar products:\n\n" + responseBody;
            }
        }
        catch (Exception e) {
            GPTResponse.setContent("Search unsuccessful. Try rephrasing your request.");
        }

        String requestBody = String.format("""
                {
                    "sourceParams": [
                        {
                            "sourceType": "WPN",
                            "source": "%s"
                        }
                    ]
                }
                """, selectedProductId);

        HttpRequest complementaryProductsRequest = HttpRequest.newBuilder()
                .uri(URI.create(
                        complementaryProductsAPILink))
                .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(complementaryProductsRequest, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            if (responseBody.isBlank() == false) {
                List<String> extractedItems = new ArrayList<>();
                responseContent += "\n\n• Complementary products:\n\n";
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                for (JsonNode item : rootNode) {
                    JsonNode recommendationResult = item.get("recommendationResult");
                    if (recommendationResult != null && recommendationResult.get("mainHeading")
                            .asText().equals("People Buy Together")) {
                        JsonNode recommendationResults = recommendationResult.get("recommendationResults");
                        if (recommendationResults != null && recommendationResults.isArray()) {
                            for (JsonNode recommendation : recommendationResults) {
                                String heading = recommendation.get("heading").asText();
                                String description = recommendation.get("description").asText();
                                extractedItems.add("   • " + heading + " - " + description);
                            }
                        }
                    }
                }
                String joinedItems = String.join("\n", extractedItems);
                joinedItems = joinedItems.substring(0, joinedItems.length() - 2);
                joinedItems = joinedItems.replaceAll("<[^>]+>", "");
                responseContent += joinedItems;
            }
        } catch (Exception e) {
            GPTResponse.setContent("Search unsuccessful. Try rephrasing your request.");
        }




        GPTResponse.setContent(responseContent);

        return GPTResponse;
    }
}
