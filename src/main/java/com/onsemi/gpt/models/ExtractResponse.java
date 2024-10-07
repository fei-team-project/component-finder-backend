package com.onsemi.gpt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ExtractResponse {
    String attributesJson;
    Component component;
    Integer countOfMessages;
}
