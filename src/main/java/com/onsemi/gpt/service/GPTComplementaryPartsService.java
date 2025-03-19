package com.onsemi.gpt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onsemi.gpt.models.GPTResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class GPTComplementaryPartsService {
    public GPTResponse<?> getResponse(String selectedProductId) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        String requestBody = String.format("""
        {
            "sourceParams": [
                {
                    "sourceType": "WPN",
                    "source": "%s"
                },
                {
                    "sourceType": "CATEGORY",
                    "source": "mosfets"
                }
            ]
        }
        """, selectedProductId) ;

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
        extractedItems.add("For the part " + selectedProductId + ", I found the following complementary parts:\n");

        // Iterácia cez pole JSON objektov
        for (JsonNode item : rootNode) {
            JsonNode recommendationResult = item.get("recommendationResult");
            if (recommendationResult != null && recommendationResult.get("mainHeading")
                    .asText().equals("People Buy Together")) {
                JsonNode recommendationResults = recommendationResult.get("recommendationResults");
                if (recommendationResults != null && recommendationResults.isArray()) {
                    for (JsonNode recommendation : recommendationResults) {
                        String heading = recommendation.get("heading").asText();
                        String description = recommendation.get("description").asText();
                        extractedItems.add("• " + heading + " - " + description);
                    }
                }
            }
        }

        extractedItems.forEach(System.out::println);

        String joinedItems = String.join("\n", extractedItems);

        joinedItems = joinedItems.substring(0, joinedItems.length() - 2);

        GPTResponse<?> response = new GPTResponse<>();
        response.setContent(joinedItems.replaceAll("<[^>]+>", ""));
        return response;
    }
}
