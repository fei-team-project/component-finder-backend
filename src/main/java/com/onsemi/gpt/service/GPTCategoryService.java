package com.onsemi.gpt.service;

import com.onsemi.gpt.exception.BadRequestException;
import com.onsemi.gpt.models.GPTRequest;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class GPTCategoryService {
    private static final ChatGPTAPI chatGPTAPI = new ChatGPTAPI();

    public ModelSelectorEnum selectModel(GPTRequest request) {
        try {
            String prompt = getPrompt(request);
            Matcher matcher = this.getAnswerMatcher(prompt);

            if (matcher.find()) {
                return ModelSelectorEnum.getModelFromNumber(Integer.parseInt(matcher.group()));
            } else {
                throw new BadRequestException("No valid category number found in the response.");
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred when calling the GPT API: " + e.getMessage(), e);
        }
    }

    public ModelSelectorEnum selectModel(GPTRequest request, String selectedProductId) {
        try {
            String prompt = getPrompt(request, selectedProductId);

            Matcher matcher = this.getAnswerMatcher(prompt);
            if (matcher.find()) {
                return ModelSelectorEnum.getModelFromNumber(Integer.parseInt(matcher.group()));
            } else {
                throw new BadRequestException("No valid category number found in the response.");
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred when calling the GPT API: " + e.getMessage(), e);
        }
    }

    private Matcher getAnswerMatcher(String prompt) throws Exception {
        // ziskanie odpovede
        String response = chatGPTAPI.generateResponseWithModel(
                prompt,
                "You are a categorization assistant. Answer only with one number.",
                "gpt-4-1106-preview"
        );

        // gpt-4o - $2.50 / 1M input tokens
        // gpt-4-1106-preview - $10.00 / 1M tokens
        // gpt-4 - $30.00 / 1M tokens

        // parsovanie JSON odpovede
        JSONObject jsonResponse = new JSONObject(response);
        String categoryText = jsonResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content").trim();

        // dostat cislo kategorie
        Pattern pattern = Pattern.compile("\\d+");
        return pattern.matcher(categoryText);

    }

    private String getPrompt(GPTRequest request) {
        return "Categorize the following request into one of the categories below. "
                + "1. If the request is about technical specifications, general part types, or component selection, including:"
                + "   - Numerical parameters (e.g., voltage, current, resistance)"
                + "   - Functional requirements (e.g., low-light performance, high current, bidirectional switching, SMA package, etc.)"
                + "   - General questions about suitable components for specific applications "
                + "2. If the request contains a specific part number (OPN or WPN) and asks for related complementary, matching, associated, or synergistic parts."
                + "3. If the request contains only a specific part number (OPN or WPN) with no additional context."
                + "4. If the request contains a specific part number (OPN or WPN) and asks for alternatives, substitutes, or similar parts."
                + "5. If the request does not fit into any of the above categories, classify it here."
                + "Request: " + request.getRequest().replace("\"", "")
                + "Category:";
    }

    private String getPrompt(GPTRequest request, String selectedProductId) {
        String prompt;

        if (!Objects.equals(selectedProductId, null)) {
            // definovanie promptu pre kategorizáciu
            prompt = "Categorize the following request based on these rules:"
                    + "2. Category 2: If the request contains a specific part number (OPN or ID) and asks for complementary, matching, associated, or synergistic parts."
                    + "3. Category 3: If the request contains a specific part number (OPN or ID) and asks for documentation, datasheets, specs, manuals, or reference materials."
                    + "4. Category 4: If the request contains a specific part number (OPN or ID) and asks for alternatives, substitutes, or similar parts."
                    + "5. Category 5: If the request contains a specific part number (OPN or ID) and asks about pricing, cost, availability, or order status."
                    + "Request: " + request.getRequest().replace("\"", "")
                    + "Category:";
        } else {
            // definovanie promptu pre kategorizáciu
            prompt = "Categorize the following request based on these rules:"
                    + "1. Category 1: If the request contains technical specifications (e.g., voltage, current, resistance) or general part types (e.g., analog_bjt, mosfet, op-amp), but ONLY if they're accompanied by specific parameters."
                    + "6. Category 6: If you are absolutely sure it is not category 1. This will be the wrong request category."
                    + "Request: " + request.getRequest().replace("\"", "")
                    + "Category:";
        }


        return prompt;
    }
}
