package com.onsemi.gpt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class APair {
    String name;
    String value;
    Range range;


    public enum Range {
        MIN,
        MAX,
        EXACT,
        STRING
    }

    public String getRangeString (Range range) {
        if(range.equals(Range.MIN)){
            return "MINIMUM - ";
        }
        if(range.equals(Range.MAX)){
            return "MAXIMUM - ";
        }
        else return "";
    }

}
