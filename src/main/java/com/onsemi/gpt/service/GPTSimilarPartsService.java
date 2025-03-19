package com.onsemi.gpt.service;

import com.onsemi.gpt.models.GPTResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@AllArgsConstructor
@Service
public class GPTSimilarPartsService {
    public GPTResponse<?> getResponse(String selectedProductId) {
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
            response.setContent("Search unsuccessful. Try rephrasing your request.");
        }

        return response;
    }
}
