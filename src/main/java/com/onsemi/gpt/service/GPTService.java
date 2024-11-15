package com.onsemi.gpt.service;

import com.onsemi.gpt.models.*;

import java.util.List;
import com.onsemi.gpt.models.GPTResponse;
import com.onsemi.gpt.models.GPTRequest;

public interface GPTService {

    GPTResponse getResponse(GPTRequest request);

    GPTResponse getFilters(GPTFiltersRequest request);

    GPTResponse getCategories(GPTRequest request);
}
