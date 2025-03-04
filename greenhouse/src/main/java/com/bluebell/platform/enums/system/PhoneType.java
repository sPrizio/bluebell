package com.bluebell.platform.enums.system;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * An enumeration of various types of phone numbers
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Schema(title = "PhoneType", name = "PhoneType", description = "Type of phone for a given phone number")
public enum PhoneType {
    MOBILE,
    HOME,
    WORK,
    OTHER
}
