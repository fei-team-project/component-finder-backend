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
    private RegexService regexService;
    private FindDocsForPartsService findDocsForPartsService;

    public GPTResponse<?> getResponse(GPTRequest request) throws Exception {
        GPTResponse<?> response;
        String selectedProductId = regexService.getOpnWpn(request);
        ModelSelectorEnum modelType = selectModel(request, selectedProductId);

        switch (modelType) {
            case ModelSelectorEnum.SEARCH_PART_BY_PARAMS:
                return GPTService.getResponse(request);
            case ModelSelectorEnum.SUGGEST_COMPLEMENTARY_PARTS:
                return GPTComplementaryPartsService.getResponse(selectedProductId);
            case ModelSelectorEnum.FIND_DOCUMENTATION_FOR_PARTS:
                return findDocsForPartsService.getResponse(selectedProductId);
            case ModelSelectorEnum.FIND_SIMILAR_PARTS:
                return GPTSimilarPartsService.getResponse(selectedProductId);
            case ModelSelectorEnum.OTHER:
                response = new GPTResponse<>();
                response.setContent("I can't answer your request");
                return response;
            default:
                response = new GPTResponse<>();
                response.setContent("I can't answer your request");
                return response;
        }
    }

    public ModelSelectorEnum selectModel(GPTRequest request, String selectedProductId) {
        return GPTCategoryService.selectModel(request, selectedProductId);
        // return LuisService.getModelId(request);
        // return EmbeddingsService.run(request.getRequest());
    }
}
