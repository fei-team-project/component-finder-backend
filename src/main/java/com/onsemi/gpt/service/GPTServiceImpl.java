package com.onsemi.gpt.service;

import com.onsemi.gpt.exception.NotFoundException;
import com.onsemi.gpt.models.*;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
@Slf4j
public class GPTServiceImpl implements GPTService {

    private final ExtractService extractService;

    private final QueryService queryService;

    private final TalkService talkService;

    @Override
    public GPTResponse<?> getResponse(GPTRequest request) {

        GPTResponse<?> response = new GPTResponse<>();

        try {
            ExtractResponse extractResponse = extractService.extractFromRequest(request.getRequest(), request.getPreviousAttributes(), request.getPreviousComponent(), request.getHistory());
            if(extractResponse.getComponent() == null){
                String humanResponse = talkService.chatResponse(null, null, null, null);
                response.setContent(humanResponse);
            }
            else {
                QueryResponse queryResponse = queryService.generateAndExecuteSQL(extractResponse.getAttributesJson(), extractResponse.getComponent());
                String humanResponse = talkService.chatResponse(extractResponse.getComponent().toString(), queryResponse.getResults().size(), extractResponse.getCountOfMessages(), queryResponse.getAttributes().size());
                response = getGptResponse(response, extractResponse, queryResponse, humanResponse);
            }
        } catch (NotFoundException e) {
            response.setContent(e.getMessage());
        } catch (Exception e){
            response.setContent("An error occurred while generating the response on OpenAI ChatGPT API side");
        }
        return response;
    }

    public GPTResponse<?> handleHeader(GPTResponse<?> response, Component component) {
        response.setHeader(component.getTableColumns());
        return response;
    }

    @Override
    public GPTResponse<?> getFilters(GPTFiltersRequest request){

        GPTResponse<?> response = new GPTResponse<>();

        try{
            ExtractResponse extractResponse = extractService.extractFromFilterRequest(request.getAttributes(), request.getComponent());
            QueryResponse queryResponse = queryService.generateAndExecuteSQL(extractResponse.getAttributesJson(), extractResponse.getComponent());
            String filterResponse = "I've updated the list of components in the table below based on the changes you made to the filters on the left side of our chat. " +
                    "Take a look and let me know if everything looks good or if you need any further adjustments";
            response = getGptResponse(response, extractResponse, queryResponse, filterResponse);
        } catch (NotFoundException e) {
            response.setContent(e.getMessage());
        } catch (Exception e){
            response.setContent("An error occurred while generating the response on OpenAI ChatGPT API side");
        }
        return response;
    }

    @Override
    public GPTResponse<?> getCategories(GPTRequest request){
        GPTResponse<?> response = new GPTResponse<>();

        try {
            int selectedCategory = GPTCategorySelector.determineCategory(request.getRequest());
            response.setContent("We have selected the category " + selectedCategory);
        } catch (Exception e) {
            response.setContent(e.getMessage());
        }

        return response;
    }

    private GPTResponse<?> getGptResponse(GPTResponse<?> response, ExtractResponse extractResponse, QueryResponse queryResponse, String humanResponse){
        response.setContent(humanResponse);
        response.setAttributes(queryResponse.getAttributes());
        response.setComponentsList(queryResponse.getResults());
        response.setComponent(extractResponse.getComponent().toString());

        response = handleHeader(response, extractResponse.getComponent());
        return response;
    }

}
