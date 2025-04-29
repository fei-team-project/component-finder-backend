package com.onsemi.gpt.service;

public enum ModelSelectorEnum {
    SEARCH_PART_BY_PARAMS,
    COMPLEMENTARY_PARTS,
    PART_NUMBER,
    SIMILAR_PARTS,
    OTHER;

    public static ModelSelectorEnum getModelFromNumber(int number) {
        if (number > 0 && number < ModelSelectorEnum.values().length) {
            return values()[number - 1];
        }

        return OTHER;
    }
}
