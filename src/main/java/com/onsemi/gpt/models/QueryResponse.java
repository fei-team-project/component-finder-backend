package com.onsemi.gpt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class QueryResponse {
    List<? extends BaseComponent> results;
    List<APair> attributes;
}
