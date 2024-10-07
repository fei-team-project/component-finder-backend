package com.onsemi.gpt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class TalkService {

    private final ChatGPTAPI chatGPTAPI;

    String chatResponse(String component, Integer countOfComponents, Integer countOfMessages, Integer countOfFilters) throws Exception {
        log.info("chatResponse({})", component);
        String humanResponseRequest;
        Random random = new Random();
        List<String> startOfReplyChange = List.of("Of course", "Sure", "Updated filters", "No problem", "Done!", "I made changes");
        String startOfSentence = "";
        if (component == null && countOfComponents == null && countOfMessages == null) {
            humanResponseRequest = "I need you to response like human would do. Tell that you (chatgpt) found no components in our offer related to person request " +
                    "that person sent you and can try to ask you again or check offered components on website";
        }
        else if(countOfComponents == 0){
            humanResponseRequest = "I need you to response like human would do. Tell that you (chatgpt) found no components with given attributes " +
                    "and person (me) can try to adjust the filters manually on left side or ask you again changed request";
        } else if (countOfFilters == 0) {
            humanResponseRequest = "I need you to response like human would do. Tell to the person that you (chatGPT) listed found components " +
                    "in table below (under the chat) and tell that you found no filters in my request, so you listed all " + component + ". Person can ask again more" +
                    "specific search criteria";
        } else  if (countOfMessages == null){
            humanResponseRequest = "I need you to response like human would do. Tell to the person that you (chatGPT) listed found components " +
                    "in table below (under the chat) with filters changed by user (me) manually in the filters on the left side of this chat";
        }
        else if (countOfMessages == 0){
            humanResponseRequest = "I need you to response like human would do. Tell to the person that you (chatGPT) listed found components in table below (under the chat) and " +
                    "applied filters are on the left side. Person (me) can change them manually or ask again";
        }
        else{
            humanResponseRequest = "I need you to response like human would do. Tell that table and filters were changed by the last message the person sent";
            startOfSentence = "Start the sentence with " + startOfReplyChange.get(random.nextInt(startOfReplyChange.size()));
        }
        String humanResponseJson = chatGPTAPI.generateResponse(humanResponseRequest, "You are assistant which responds with maximum of 3 sentences." + startOfSentence, "");

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode humanResponseAttributesNode = objectMapper.readTree(humanResponseJson);

        return humanResponseAttributesNode.path("choices").path(0).path("message").path("content").asText();
    }
}
