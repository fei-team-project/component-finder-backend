package com.onsemi.gpt.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class RequestHistory {
    private List<String> history = new ArrayList<>();

    public void addToHistory(String request) {
        history.add(request);
    }

    public List<String> getHistory() {
        int size = history.size();
        if (size <= 1) {
            return new ArrayList<>();
        } else {
            // Return a sublist excluding the last element - current request
            return history.subList(0, size - 1);
        }
    }
}