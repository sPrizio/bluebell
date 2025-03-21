package com.bluebell.platform.enums;

import com.bluebell.platform.exceptions.enums.EnumValueNotFoundException;

import java.util.Arrays;

/**
 * Base enum giving core attributes used throughout the app
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
public interface GenericEnum<E> {

    /**
     * Obtains the enum code, unique id used for fetching
     *
     * @return code
     */
    String getCode();

    /**
     * Obtains the enum label, display string
     *
     * @return display string
     */
    String getLabel();

    /**
     * Obtains the associated enum by its code
     *
     * @param clazz enum class
     * @param code enum code
     * @return {@link E}
     */
    static <E extends Enum<E> & GenericEnum<E>> E getByCode(final Class<E> clazz, final String code) {
        return Arrays
                .stream(clazz.getEnumConstants())
                .filter(e -> e.getCode().equals(code)).findFirst()
                .orElseThrow(() -> new EnumValueNotFoundException(String.format("No enum value : %s", code)));
    }
}
