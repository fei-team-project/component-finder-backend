package com.onsemi.gpt.rest;

import com.onsemi.gpt.models.GPTFiltersRequest;
import com.onsemi.gpt.models.GPTResponse;
import com.onsemi.gpt.service.EmbeddingsService;
import com.onsemi.gpt.service.GPTService;
import com.onsemi.gpt.service.ModelSelectorRegexService;
import com.onsemi.gpt.service.ModelSelectorService;
import com.onsemi.gpt.models.GPTRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gpt")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class GPTController {

    private final GPTService service;
    private ModelSelectorService modelSelectorService;
    private ModelSelectorRegexService modelSelectorRegexService;

    @PostMapping("/request/new")
    public GPTResponse getGPTResponse(@RequestBody GPTRequest request) throws Exception {
        //return this.modelSelectorService.getResponse(request);
        return this.modelSelectorRegexService.getResponse(request);
    }

    @PostMapping("/request/update")
    public GPTResponse getFiltersResponse(@RequestBody GPTFiltersRequest request){
        return this.service.getFilters(request);
    }

    @GetMapping("/embeddings/test")
    public String testEmbeddingsSuccessRate(){
        double success = EmbeddingsService.testSuccess();
        String res = "The success rate of Embeddings is: " + success + "%";
        System.out.println(res);
        return res;
    }
}
