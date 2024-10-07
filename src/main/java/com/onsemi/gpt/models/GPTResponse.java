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
public class GPTResponse <T extends BaseComponent>{

    String content;
    String component;
    List<APair> attributes;
    List<T> components;
    List<String> header;

    public <U extends BaseComponent> void setComponentsList(List<U> components) {
        this.components = (List<T>) components;
    }
}
