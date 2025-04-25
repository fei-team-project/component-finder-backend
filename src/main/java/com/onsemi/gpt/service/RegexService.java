package com.onsemi.gpt.service;

import com.onsemi.gpt.models.GPTRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class RegexService {
    public String getOpnWpn(GPTRequest request) {
        String regex = "(?=[A-Z0-9.-]*\\d)([A-Z0-9.-]{5,})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(request.getRequest().replace("\"", ""));

        if (matcher.find()) {
            return matcher.group(1);    // return the first matching group
        }

        return null;
    }
}
