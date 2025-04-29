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
    private SimilarPartsService SimilarPartsService;
    private ComplementaryPartsService ComplementaryPartsService;
    private GPTCategoryService GPTCategoryService;
    private RegexService regexService;
    private PartNumberService PartNumberService;

    public GPTResponse<?> getResponse(GPTRequest request) throws Exception {
        GPTResponse<?> response;
        String selectedProductId = regexService.getOpnWpn(request);
        ModelSelectorEnum modelType = selectModel(request, selectedProductId);

        switch (modelType) {
            case ModelSelectorEnum.SEARCH_PART_BY_PARAMS:
                return GPTService.getResponse(request);
            case ModelSelectorEnum.COMPLEMENTARY_PARTS:
                return ComplementaryPartsService.getResponse(selectedProductId);
            case ModelSelectorEnum.PART_NUMBER:
                return PartNumberService.getResponse(selectedProductId);
            case ModelSelectorEnum.SIMILAR_PARTS:
                return SimilarPartsService.getResponse(selectedProductId);
            case ModelSelectorEnum.OTHER:
                response = new GPTResponse<>();
                response.setContent("Thank you for your request. However, it appears that your request does not match any of the standard categories we support (technical details, part numbers, complementary parts, or similar parts).\n" +
                        "You might want to reframe or clarify your request so we can assist you more effectively within ONSEMI’s product scope.");
                return response;
            default:
                response = new GPTResponse<>();
                response.setContent("Thank you for your request. However, it appears that your request does not match any of the standard categories we support (technical details, part numbers, complementary parts, or similar parts).\n" +
                        "You might want to reframe or clarify your request so we can assist you more effectively within ONSEMI’s product scope.");
                return response;
        }
    }

    public ModelSelectorEnum selectModel(GPTRequest request, String selectedProductId) {
        return GPTCategoryService.selectModel(request, selectedProductId); // <- GPT + regex
        //return GPTCategoryService.selectModel(request);  // <- GPT bez regexu
        //return LuisService.getModelId(request);
        //return EmbeddingsService.run(request.getRequest());
    }
}
