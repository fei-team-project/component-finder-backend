package com.onsemi.gpt.service;

import com.onsemi.gpt.exception.BadRequestException;
import com.onsemi.gpt.models.GPTRequest;
import com.onsemi.gpt.models.GPTResponse;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class ModelSelectorService {

    private GPTService GPTService;
    private ChatGPTAPI chatGPTAPI;

    public GPTResponse<?> getResponse(GPTRequest request) {
        GPTResponse<?> response;
        int modelId = selectModel(request);
        switch (modelId) {
            case 1:
                response = new GPTResponse<>();
                response.setContent("Model 1");
                return response;
                //return GPTService.getResponse(request);
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
        try {
            // definovanie promptu pre kategorizáciu
            String prompt = "Categorize this text into one of the categories:"
                    + "1. Search for a part by parameters."
                    + "2. Suggest complementary parts."
                    + "3. Find documentation for the part."
                    + "4. Find similar parts."
                    + "5. Find out the availability and price of the part."
                    + "Text:" + request.getRequest().replace("\"", "")
                    + "Category:";

            // ziskanie odpovede
            String response = chatGPTAPI.generateResponseWithModel(
                    prompt,
                    "You are a categorization assistant. Answer only with one number.",
                    "gpt-4-1106-preview"
            );
            // gpt-4-1106-preview - $10.00 / 1M tokens
            // gpt-4o - $2.50 / 1M input tokens
            // gpt-4 - $30.00 / 1M tokens

            // parsovanie JSON odpovede
            JSONObject jsonResponse = new JSONObject(response);
            String categoryText = jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content").trim();

            // dostat cislo kategorie
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(categoryText);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            } else {
                throw new BadRequestException("No valid category number found in the response.");
            }

        } catch (Exception e) {
            throw new RuntimeException("An error occurred when calling the GPT API: " + e.getMessage(), e);
        }
    }
}
