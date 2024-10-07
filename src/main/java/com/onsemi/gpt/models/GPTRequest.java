package com.onsemi.gpt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GPTRequest {
    String request;
    List<APair> previousAttributes;
    String previousComponent;
    List<String> history;
}
