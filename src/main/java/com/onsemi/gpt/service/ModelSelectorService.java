package com.onsemi.gpt.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onsemi.gpt.models.GPTRequest;
import com.onsemi.gpt.models.GPTResponse;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ModelSelectorService {

    private GPTService GPTService;

    public GPTResponse<?> getResponse(GPTRequest request) {
        GPTResponse<?> response;
        int modelId = selectModel(request);
        modelId = 1;
        switch (modelId) {
            case 0:
                return GPTService.getResponse(request);
            case 1:
                response = new GPTResponse<>();
                response.setContent("Embeddings: " + EmbeddingsService.run(request.getRequest()));
                return response;
            case 2:
                response = new GPTResponse<>();
                response.setContent("LUIS: " + LuisService.getModelId(request));
                return response;
            case 3:
                response = new GPTResponse<>();
                response.setContent("GPT: " + GPTCategoryService.selectModel(request));
                return response;
            case 4:
                response = new GPTResponse<>();
                response.setContent("Model 4");
                return response;
            default:
                return GPTService.getResponse(request);
        }
    }

    public int selectModel(GPTRequest request) {
        try {
            URI uri = new URI("https://www.randomnumberapi.com/api/v1.0/random?min=0&max=4&count=1");
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.toString());
                if (jsonNode.isArray() && jsonNode.size() > 0) {
                    int modelId = jsonNode.get(0).asInt();
                    return modelId;
                }
            }
        } catch (Exception exception) {
            return 0;
        }
        return 0;
    }
}
