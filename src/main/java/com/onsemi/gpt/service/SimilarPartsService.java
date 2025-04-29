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
public class SimilarPartsService {
    public GPTResponse<?> getResponse(String selectedProductId) {
        String searchLink = String.format("https://www.onsemi.com/design/tools-software/product-recommendation-tools-plus/api/similarProducts?generalPartNumber=%s", selectedProductId);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = this.getHttpRequest(searchLink);

        HttpResponse<String> httpResponse;
        GPTResponse<?> response = new GPTResponse<>();

        try {
            httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.body().isBlank()) {
                searchLink = String.format("https://www.onsemi.com/design/tools-software/product-recommendation-tools-plus/api/similarProductsByOpn?orderablePartNumber=%s", selectedProductId);
                httpRequest = this.getHttpRequest(searchLink);

                httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            }

            response.setContent(httpResponse.body().isBlank() ? "We’re sorry, but no similar parts were found for the provided OPN/WPN: " + selectedProductId + ". Please verify that the part number is correct." : httpResponse.body());
        } catch (Exception e) {
            response.setContent("We’re sorry, but no similar parts were found for the provided OPN/WPN: " + selectedProductId + ". Please verify that the part number is correct.");
        }

        return response;
    }

    private HttpRequest getHttpRequest(String searchLink) {
        return HttpRequest.newBuilder()
                .uri(URI.create(searchLink))
                .GET()
                .build();
    }
}
