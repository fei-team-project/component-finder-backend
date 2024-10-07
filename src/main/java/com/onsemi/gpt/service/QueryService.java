package com.onsemi.gpt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onsemi.gpt.exception.BadRequestException;
import com.onsemi.gpt.exception.NotFoundException;
import com.onsemi.gpt.models.APair;
import com.onsemi.gpt.models.BaseComponent;
import com.onsemi.gpt.models.Component;
import com.onsemi.gpt.models.QueryResponse;
import com.onsemi.gpt.repository.ComponentRepository;
import com.onsemi.gpt.service.synonymsManagers.AttributesSynonymManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
@Slf4j
public class QueryService {

    private final ComponentRepository repository;

    private final AttributesSynonymManager attributesSynonymManager;

    public QueryResponse generateAndExecuteSQL(String attributesJson, Component component) throws Exception {
        log.info("generateAndExecuteSQL()");

        String tableName = component.getTableName();

        AbstractMap.SimpleEntry<String, List<APair>> sqlStringAndAttributesList = generateSql(attributesJson, tableName);

        String sql = sqlStringAndAttributesList.getKey();

        List<? extends BaseComponent> results = this.executeQuery(component, sql);

        return new QueryResponse(results, sqlStringAndAttributesList.getValue());
    }

    public AbstractMap.SimpleEntry<String, List<APair>> generateSql(String jsonString, String tableName) throws Exception {
        log.info("generateSql(" + jsonString + ")");

        String jsonSubstring = extractJsonSubstring(jsonString);

        if (jsonSubstring != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonSubstring);
            List<APair> pairs = new ArrayList<>();

            StringBuilder sql = new StringBuilder("SELECT * FROM \"");
            sql.append(tableName);
            sql.append("\" WHERE ");

            jsonNode.fields().forEachRemaining(entry -> {
                String fieldName = entry.getKey();
                JsonNode valueNode = entry.getValue();
                String normalizedValue = "";
                APair.Range range;

                if(fieldName.equalsIgnoreCase("Error")){
                    log.debug("No attributes found, search for all {}", tableName);
                }
                else if(fieldName.equalsIgnoreCase("Description")){
                    log.debug("Description node skipped");
                }
                else if (isNumber(valueNode.toString())) {
                    normalizedValue = valueNode.toString();
                    if (fieldName.startsWith("MINIMUM - ")) {
                        range = APair.Range.MIN;
                        fieldName = fieldName.substring(10); // Remove the "MINIMUM - " prefix
                        sql.append("\"").append(fieldName).append("\" >= ").append(normalizedValue).append(" AND ");
                    } else if (fieldName.startsWith("MAXIMUM - ")) {
                        range = APair.Range.MAX;
                        fieldName = fieldName.substring(10); // Remove the "MAXIMUM - " prefix
                        sql.append("\"").append(fieldName).append("\" <= ").append(normalizedValue).append(" AND ");
                    } else {
                        range = APair.Range.EXACT;
                        sql.append("\"").append(fieldName).append("\" = ").append(normalizedValue).append(" AND ");
                    }
                    APair pair = new APair(fieldName, normalizedValue, range);
                    pairs.add(pair);
                } else if (valueNode.isTextual()) {
                    String searchValue = valueNode.asText();
                    if (!searchValue.isEmpty()) {
                        normalizedValue = attributeValueSynonym(searchValue.toLowerCase());

                         sql.append("\"").append(fieldName).append("\" ILIKE '%").append(normalizedValue).append("%' AND ");

                        APair pair = new APair(fieldName, normalizedValue, APair.Range.STRING);
                        pairs.add(pair);
                    }
                }

            });
            if (sql.length() > 7) {
                sql.setLength(sql.length() - 5);
            } else {
                sql.setLength(sql.length() - 8);
            }

            log.info("This SQL will be executed {}", sql);

            return new AbstractMap.SimpleEntry<>(sql.toString(), pairs);
        } else {
            throw new NotFoundException("Error occurred while extracting attributes from request");
        }
    }


    private static String extractJsonSubstring(String input) {
        int jsonStartIndex = input.indexOf('{');

        if (jsonStartIndex != -1) {
            int jsonEndIndex = input.lastIndexOf('}');
            if (jsonEndIndex != -1) {
                return input.substring(jsonStartIndex, jsonEndIndex + 1);
            }
        }

        return null;
    }

    public static boolean isNumber(String value) {
        try {
            Double.parseDouble(value.replace("\"", ""));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String attributeValueSynonym(String originalValue) {
        log.info("attributeSynonym(" + originalValue + ")");
        originalValue =originalValue.toLowerCase();
        if (attributesSynonymManager.getSiliconCarbideSynonyms().contains(originalValue)) {
            return "Silicon Carbide";
        }
        return originalValue;
    }

    private <T extends BaseComponent> List<T> executeQuery(Component component, String sql) throws Exception{
        log.info("executeQuery(" + sql + ")");
        if (containsUnsafeKeywords(sql)){
            throw new BadRequestException("Potentially unsafe input which can violate SQL injection");
        }
        return repository.executeQueryForComponent(sql, component.getBaseComponent().getClass());
    }

    private static final Pattern UNSAFE_KEYWORDS_PATTERN = Pattern.compile(
            "\\b(?:DROP|DELETE|TRUNCATE|ALTER|INSERT|UPDATE|MERGE|EXEC(?:UTE)?|CALL|CREATE|GRANT|REVOKE)\\b",
            Pattern.CASE_INSENSITIVE);

    public static boolean containsUnsafeKeywords(String sql) {
        return UNSAFE_KEYWORDS_PATTERN.matcher(sql).find();
    }
}
