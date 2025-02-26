package com.onsemi.gpt.service;

import com.onsemi.gpt.models.GPTRequest;
import com.onsemi.gpt.models.GPTResponse;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@AllArgsConstructor
@Service
public class GPTComplementaryPartsService {
    private final ChatGPTAPI chatGPTAPI;

    public GPTResponse<?> getResponse(GPTRequest request) throws Exception {
        String prompt = "I need to select the ID of product from this request. The ID is a continuous string without spaces," +
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

        String searchLink = selectedProductId.isBlank()
                ? String.format("https://www.onsemi.com/design/tools-software/product-recommendation-tools-plus/api/similarProducts?generalPartNumber=%s", selectedProductId)
                : String.format("https://www.onsemi.com/design/tools-software/product-recommendation-tools-plus/api/similarProductsByOpn?orderablePartNumber=%s", selectedProductId);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(searchLink))
                .GET()
                .build();

        HttpResponse<String> httpResponse;
        GPTResponse<?> response = new GPTResponse<>();

        try {
            httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            response.setContent(httpResponse.body());
        } catch (Exception e) {
            response.setContent("Hľadanie neúspešné. Skús svoju požiadavku sformulovať inak.");
        }

        return response;
    }
}
