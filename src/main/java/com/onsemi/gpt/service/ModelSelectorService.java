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
        ModelSelectorEnum modelType = selectModel(request);
        switch (modelType) {
            case ModelSelectorEnum.SEARCH_PART_BY_PARAMS:
                response = new GPTResponse<>();
                response.setContent("Model 1");
                return GPTService.getResponse(request);
            case ModelSelectorEnum.SUGGEST_COMPLEMENTARY_PARTS:
                response = new GPTResponse<>();
                response.setContent("Model 2");
                return response;
            case ModelSelectorEnum.FIND_DOCUMENTATION_FOR_PARTS:
                response = new GPTResponse<>();
                response.setContent("Model 3");
                return response;
            case ModelSelectorEnum.FIND_SIMILAR_PARTS:
                response = new GPTResponse<>();
                response.setContent("Model 4");
                return response;
            case ModelSelectorEnum.CHECK_AVAILABILITY_AND_PRICE:
                response = new GPTResponse<>();
                response.setContent("Model 5");
                return response;
            default:
                response = new GPTResponse<>();
                response.setContent("Ziadny model");
                return response;
        }
    }

    public ModelSelectorEnum selectModel(GPTRequest request) {
        return GPTCategoryService.selectModel(request);
        //return LuisService.getModelId(request);
        //return EmbeddingsService.run(request.getRequest());
    }
}