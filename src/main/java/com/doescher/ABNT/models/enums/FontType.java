package com.doescher.ABNT.models.enums;

import com.doescher.ABNT.constants.AbntConstants;
import lombok.Getter;

@Getter
public enum FontType {
    ARIAL(AbntConstants.FONT_ARIAL),
    TIMES_NEW_ROMAN(AbntConstants.FONT_TIMES);

    private final String familyName;

    FontType(String familyName){
        this.familyName = familyName;
    }
}
