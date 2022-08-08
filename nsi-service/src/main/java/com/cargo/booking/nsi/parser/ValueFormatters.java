package com.cargo.booking.nsi.parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValueFormatters {

    public static ValueFormatter checkChar() {
        return (element, value) -> value.toString().trim().isEmpty() || value.length() != 1 ? null : value.toString();
    }

    public static ValueFormatter trimSpace() {
        return (element, value) -> value.toString().replaceAll("\\s+", "");
    }
}
