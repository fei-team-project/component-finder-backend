package com.onsemi.gpt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onsemi.gpt.exception.BadRequestException;
import com.onsemi.gpt.exception.NotFoundException;
import com.onsemi.gpt.models.APair;
import com.onsemi.gpt.models.Component;
import com.onsemi.gpt.models.ExtractResponse;
import com.onsemi.gpt.service.synonymsManagers.SynonymManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ExtractService {

    ChatGPTAPI chatGPTAPI;

    SynonymManager synonymManager;

    public ExtractResponse extractFromRequest(String request, List<APair> previousAttributes, String previousComponent, List<String> history) throws Exception {
        log.info("extractFromRequest()");

        String componentName;
        Component component;
        String previousAttributesString;
        boolean tableChanged = false;
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            String componentRequest = "Rules:" +
                    "1. Format your response to one node JSON. " +
                    "2. Extract only components family type and put it into 'family type' node in JSON. " +
                    "3. Components family type should be from this list: [MOSFET, POWER_SUPPLY, PIM, IGBT, IPM, IMAGE_SENSORS, " +
                    "OP_AMP, SMART_POWER_STAGE, SMART_SWITCH, GATE_DRIVER, JFET, ANALOG_BJT, RF_BJT, POWER_DIODE, SMALL_SIGNAL_DIODE, " +
                    "ZENER_DIODE, COMPARATOR, DIGITAL_BJT, PROTECTED_POWER_SWITCHES, CLOCK_GENERATION], but if you are not sure it is in request, return NONE" +
                    "Request:" + request;

            String jsonString = chatGPTAPI.generateResponse(componentRequest, "You are an assistant, and you reply with JSON.", ", \"response_format\": { \"type\": \"json_object\" }");

            JsonNode jsonNode = objectMapper.readTree(jsonString);
            componentName = extractComponentName(jsonNode.path("choices").path(0).path("message").path("content").asText());
            component = getComponent(componentName);
            if (!component.getTableName().equalsIgnoreCase(previousComponent)) {
                tableChanged = true;
            }
        }
        catch (Exception e){
            if (previousComponent == null || previousComponent.isEmpty()){
                log.info("Component name was not found in request and previous component name is empty");
                return new ExtractResponse("", null, null);
            }
            log.info("Using component from previous request: {}", previousComponent);
            component = getComponentFromTableName(previousComponent);
        }

        if(previousAttributes == null || tableChanged) {
            log.info("No previous attributes are used, only new extracted ones");
            previousAttributesString = "";
        }
        else{
            log.info("Previous extracted attributes will be merged with new extracted ones");
            StringBuilder result = new StringBuilder();
            for (APair pair : previousAttributes) {
                result.append(pair.getRangeString(pair.getRange()))
                        .append(pair.getName())
                        .append(": ")
                        .append(pair.getValue())
                        .append(", ");

            }

            if (result.length() > 0) {
                result.setLength(result.length() - 2);
            }

            previousAttributesString = result.toString();
            log.info("Previous extracted attributes" + previousAttributesString);
        }
        int countOfMessages = 0;
        if (history != null){
            if(!history.isEmpty() && history.get(0) != "") {
                countOfMessages = history.size();
            }
        }
        return getExtractResponse(component, request, objectMapper, previousAttributesString, countOfMessages);
    }

    public ExtractResponse extractFromFilterRequest(List<APair> attributes, String tableName) throws Exception{
        Component component = getComponentFromTableName(tableName);
        String request = convertAPairListToString(attributes);
        ObjectMapper objectMapper = new ObjectMapper();

        return getExtractResponse(component, request, objectMapper, "", null);
    }

    private ExtractResponse getExtractResponse(Component component, String request, ObjectMapper objectMapper, String previousAttributes, Integer countOfMessages) throws Exception {
        String componentAttributes = component.getAttributesAsString();

        String firstRule;
        if (Objects.equals(previousAttributes, "")){
            firstRule = "Generate new JSON";
        }
        else firstRule = "Here are the previous attributes, merge them with the new one you will extract in the JSON: " + previousAttributes +
                ". ";

        String attributesInput = "Generate a JSON response according to the following rules:" +
                " 1." + firstRule +
                " 2. You have to map data from the request to given attributes, if present." +
                " 3. Preserve the exact attribute names, even with brackets and MIN/MAX." +
                " 4. Only add numbers to numerical attributes, do not add units." +
                " 5. Numeric nodes with MAXIMUM prefix are for maximal value of attribute (below, at most, no more than,...)" +
                " 6. Numeric nodes with MINIMUM prefix are for minimal value of attribute (over, at least, no less than,...)" +
                "Attributes of" + component.name() + "to look for: '" + componentAttributes + "' The request: '" + request + "'";

        String searchJson = chatGPTAPI.generateResponse(attributesInput, "You are an assistant, and you reply with JSON, where every attribute is one node. If node value is null, delete that node.", ", \"response_format\": { \"type\": \"json_object\" }");

        JsonNode jsonAttributesNode = objectMapper.readTree(searchJson);

        String attributesString = jsonAttributesNode.path("choices").path(0).path("message").path("content").asText();

        return new ExtractResponse(attributesString, component, countOfMessages);
    }

    private static String convertAPairListToString(List<APair> aPairList) {
        StringBuilder result = new StringBuilder();

        for (APair aPair : aPairList) {
            String range = "";
            if (aPair.getRange() == APair.Range.MIN){
                range = "MINIMUM - ";
            } else if (aPair.getRange() == APair.Range.MAX){
                range = "MAXIMUM - ";
            } else if (aPair.getRange() == APair.Range.EXACT){
                range = "EXACT - ";
            }

            result.append("Name: ").append(range).append(aPair.getName())
                    .append(", Value: ").append(aPair.getValue())
                    .append(", ");
        }

        return result.toString();
    }

    private Component getComponent(String componentName) throws Exception {
        log.info("getComponent(" + componentName + ")");
        componentName = componentName.toUpperCase();


        try{
            return getComponentFromTableName(componentName);
        }
        catch(Exception e) {
            for (Component component : Component.values()) {
                if (matchesSynonym(componentName, component)) {
                    return component;
                }
            }
        }
        throw new NotFoundException("Component '" + componentName + "' not found");
    }

    private boolean matchesSynonym(String componentName, Component component) {
        String tableName = component.getTableName().toUpperCase();
        Set<String> synonyms = synonymManager.getSynonymsForComponent(component);

        return componentName.contains(tableName) || synonyms.contains(componentName);
    }

    private static String extractComponentName(String input) throws Exception {
        log.info("extractComponentName(" + input + ")");
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(input);
        JsonNode familyTypeNode = jsonNode.get("family type");
        JsonNode familyTypeNodeChange = jsonNode.get("family_type");

        if (familyTypeNode != null && familyTypeNode.isTextual()) {
            return familyTypeNode.asText().trim();
        } else if (familyTypeNodeChange != null && familyTypeNodeChange.isTextual()) {
            return familyTypeNodeChange.asText().trim();
        } else {
            throw new BadRequestException("Error processing JSON response");
        }

    }

    private static Component getComponentFromTableName(String tableName) throws Exception{
        log.info( "getComponentFromTableName(" + tableName+ ")");
        if (Objects.equals(tableName, "MOSFET")) {
            return Component.MOSFET;
        } else if (Objects.equals(tableName, "ANALOG_BJT")) {
            return Component.ANALOG_BJT;
        } else if (Objects.equals(tableName, "CLOCK_GENERATION")) {
            return Component.CLOCK_GENERATION;
        } else if (Objects.equals(tableName, "COMPARATOR")) {
            return Component.COMPARATOR;
        } else if (Objects.equals(tableName, "DIGITAL_BJT")) {
            return Component.DIGITAL_BJT;
        } else if (Objects.equals(tableName, "GATE_DRIVER")) {
            return Component.GATE_DRIVER;
        } else if (Objects.equals(tableName, "IGBT")) {
            return Component.IGBT;//38766
        } else if (Objects.equals(tableName, "IMAGE_SENSORS")) {
            return Component.IMAGE_SENSORS;
        } else if (Objects.equals(tableName, "IPM")) {
            return Component.IPM;
        } else if (Objects.equals(tableName, "JFET")) {
            return Component.JFET;
        } else if (Objects.equals(tableName, "OP_AMP")) {
            return Component.OP_AMP;
        } else if (Objects.equals(tableName, "PIM")) {
            return Component.PIM;
        } else if (Objects.equals(tableName, "POWER_SUPPLY")) {
            return Component.POWER_SUPPLY;
        } else if (Objects.equals(tableName, "POWER_DIODE")) {
            return Component.POWER_DIODE;
        } else if (Objects.equals(tableName, "PROTECTED_POWER_SWITCHES")) {
            return Component.PROTECTED_POWER_SWITCHES;
        } else if (Objects.equals(tableName, "RF_BJT")) {
            return Component.RF_BJT;
        } else if (Objects.equals(tableName, "SMALL_SIGNAL_DIODE")) {
            return Component.SMALL_SIGNAL_DIODE;
        } else if (Objects.equals(tableName, "SMART_POWER_STAGE")) {
            return Component.SMART_POWER_STAGE;
        } else if (Objects.equals(tableName, "SMART_SWITCH")) {
            return Component.SMART_SWITCH;
        } else if (Objects.equals(tableName, "ZENER_DIODE")) {
            return Component.ZENER_DIODE;
        }
        log.info("Component family type was not found in request");
        throw new NotFoundException("Component name was not found in request");
    }
}
