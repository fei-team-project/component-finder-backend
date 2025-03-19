package com.onsemi.gpt.service;

import com.onsemi.gpt.models.GPTRequest;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Configuration
@Slf4j
public class ChatGPTAPI {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String OPENAI_API_KEY = dotenv.get("OPENAI_API_KEY");
    private static final String GPT_ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL_NAME = "gpt-4-1106-preview";

    public String generateResponse(String prompt, String content, String responseFormat) throws Exception {
        return generateResponse(prompt, content, responseFormat, MODEL_NAME);
    }

    public String generateResponseWithModel(String prompt, String content, String modelName) throws Exception {
        return generateResponse(prompt, content, "", modelName);
    }

    public String generateResponse(String prompt, String content, String responseFormat, String modelName) throws Exception {
        log.info("generateResponse(" + prompt + ")");
        URL obj = new URL(GPT_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + OPENAI_API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        String messagesHistory = "";
        String body = "{\"model\": \"" + modelName + "\", " +
                "\"messages\": [{\"role\": \"system\", \"content\": \"" + content + "\"}, "
                + messagesHistory
                + "{\"role\": \"user\", \"content\": \"" + prompt + "\"}]," +
                " \"temperature\": 0" + responseFormat + "}";

        log.info("body string" + body);
        connection.setDoOutput(true);

        try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
            writer.write(body);
            writer.flush();
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }


    public String getRequestId(GPTRequest request) throws Exception {
        String prompt = "I need to select the ID of product from this request. The ID is a continuous string without spaces," +
                " consisting of both letters and numbers, with a minimum length of 6. Return the string from this request. " +
                "If there isn't an id return empty string."
                + "Request: " + request.getRequest().replace("\"", "");

        String gptResponse = this.generateResponseWithModel(
                prompt,
                "You have to select product id from this request. Answer only with the ID nothing else.",
                "gpt-4-1106-preview"
        );

        JSONObject jsonResponse = new JSONObject(gptResponse);

        return jsonResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content").trim();
    }
}
