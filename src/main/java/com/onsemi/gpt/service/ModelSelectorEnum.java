package com.onsemi.gpt.service;

public enum ModelSelectorEnum {
    DEFAULT,
    SEARCH_PART_BY_PARAMS,
    SUGGEST_COMPLEMENTARY_PARTS,
    FIND_DOCUMENTATION_FOR_PARTS,
    FIND_SIMILAR_PARTS,
    CHECK_AVAILABILITY_AND_PRICE;

    public static ModelSelectorEnum getModelFromNumber(int number) {
        if (number > 0 && number < ModelSelectorEnum.values().length) {
            return values()[number];
        }

        return DEFAULT;
    }
}
