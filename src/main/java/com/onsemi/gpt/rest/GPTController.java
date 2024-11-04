package com.onsemi.gpt.rest;

import com.onsemi.gpt.models.GPTFiltersRequest;
import com.onsemi.gpt.models.GPTResponse;
import com.onsemi.gpt.service.GPTService;
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

    @PostMapping("/request/new")
    public GPTResponse getGPTResponse(@RequestBody GPTRequest request){
        return this.modelSelectorService.getResponse(request);
    }

    @PostMapping("/request/update")
    public GPTResponse getFiltersResponse(@RequestBody GPTFiltersRequest request){
        return this.service.getFilters(request);
    }
}
