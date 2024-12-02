package com.onsemi.gpt.service;

import com.onsemi.gpt.models.GPTRequest;
import com.onsemi.gpt.models.GPTResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ModelSelectorService {

    private GPTService GPTService;
    private GPTCategoryService GPTCategoryService;

    public GPTResponse<?> getResponse(GPTRequest request) {
        GPTResponse<?> response;
        int modelId = selectModel(request);
        switch (modelId) {
            case 1:
                response = new GPTResponse<>();
                response.setContent("Model 1");
                return GPTService.getResponse(request);
            case 2:
                response = new GPTResponse<>();
                response.setContent("Model 2");
                return response;
            case 3:
                response = new GPTResponse<>();
                response.setContent("Model 3");
                return response;
            case 4:
                response = new GPTResponse<>();
                response.setContent("Model 4");
                return response;
            case 5:
                response = new GPTResponse<>();
                response.setContent("Model 5");
                return response;
            default:
                response = new GPTResponse<>();
                response.setContent("Ziadny model");
                return response;
                //return GPTService.getResponse(request);
        }
    }

    public int selectModel(GPTRequest request) {
        //return GPTCategoryService.selectModel(request);
        return LuisService.getModelId(request);
        //return EmbeddingsService.run(request.getRequest());
    }
}