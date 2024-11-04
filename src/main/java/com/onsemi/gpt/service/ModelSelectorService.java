package com.onsemi.gpt.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.onsemi.gpt.models.GPTRequest;
import com.onsemi.gpt.models.GPTResponse;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ModelSelectorService {

    private GPTService GPTService;

    public GPTResponse<?> getResponse(GPTRequest request) {
        Random random = new Random();
        int modelId = random.nextInt(2);
        switch (modelId) {
            case 0:
                return GPTService.getResponse(request);
            case 1:
                GPTResponse<?> response = new GPTResponse<>();
                response.setContent("Hello, World!");
                return response;
            default:
                return new GPTResponse<>();
        }
    }
}
