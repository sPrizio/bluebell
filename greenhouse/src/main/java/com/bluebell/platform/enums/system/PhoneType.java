package com.bluebell.platform.enums.system;

import com.bluebell.platform.enums.GenericEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * An enumeration of various types of phone numbers
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
@Schema(title = "PhoneType", name = "PhoneType", description = "Type of phone for a given phone number")
public enum PhoneType implements GenericEnum<PhoneType> {
    MOBILE("MOBILE", "Mobile"),
    HOME("HOME", "Home"),
    WORK("WORK", "Work"),
    OTHER("OTHER", "Other");

    private final String code;

    private final String label;

    PhoneType(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
