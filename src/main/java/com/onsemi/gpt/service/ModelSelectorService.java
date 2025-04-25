package com.onsemi.gpt.service;

import com.onsemi.gpt.models.GPTRequest;
import com.onsemi.gpt.models.GPTResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ModelSelectorService {

    private GPTService GPTService;
    private GPTSimilarPartsService GPTSimilarPartsService;
    private GPTComplementaryPartsService GPTComplementaryPartsService;
    private GPTCategoryService GPTCategoryService;
    private ChatGPTAPI chatGPTAPI;

    public GPTResponse<?> getResponse(GPTRequest request) throws Exception {
        GPTResponse<?> response;
        //String selectedProductId = chatGPTAPI.getRequestId(request);
        String selectedProductId = "";
        ModelSelectorEnum modelType = selectModel(request);

        switch (modelType) {
            case ModelSelectorEnum.SEARCH_PART_BY_PARAMS:
                return GPTService.getResponse(request);
            case ModelSelectorEnum.SUGGEST_COMPLEMENTARY_PARTS:
                return GPTComplementaryPartsService.getResponse(selectedProductId);
            case ModelSelectorEnum.FIND_DOCUMENTATION_FOR_PARTS:
                response = new GPTResponse<>();
                response.setContent("Kategória 3");
                return response;
            case ModelSelectorEnum.FIND_SIMILAR_PARTS:
                return GPTSimilarPartsService.getResponse(selectedProductId);
            case ModelSelectorEnum.CHECK_AVAILABILITY_AND_PRICE:
                response = new GPTResponse<>();
                response.setContent("Kategória 5");
                return response;
            default:
                response = new GPTResponse<>();
                response.setContent("Žiadna kategória");
                return response;
        }
    }

    public ModelSelectorEnum selectModel(GPTRequest request) {
        return GPTCategoryService.selectModel(request);
        // return LuisService.getModelId(request);
        // return EmbeddingsService.run(request.getRequest());
    }
}
