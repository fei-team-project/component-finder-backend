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
            response.setContent(httpResponse.body());
        } catch (Exception e) {
            response.setContent("Search unsuccessful. Try rephrasing your request.");
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
