package com.onsemi.gpt.service.synonymsManagers;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
public class AttributesSynonymManager {

    private final List<String> siliconCarbideSynonyms;

    public AttributesSynonymManager(){

        this.siliconCarbideSynonyms = List.of(
                "sic",
                "sic mosfet",
                "silicon carbide",
                "silicon carbide mosfet"
        );
    }
}
