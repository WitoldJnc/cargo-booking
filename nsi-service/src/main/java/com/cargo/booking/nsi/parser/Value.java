package com.cargo.booking.nsi.parser;

public final class Value {
    private final CharSequence rawValue;
    private final ElementSSIM element;

    Value(CharSequence rawValue, ElementSSIM element) {
        this.rawValue = rawValue;
        this.element = element;
    }

    public int getSize() {
        return rawValue.length();
    }

    public String getRawValue() {
        return rawValue.toString().trim();
    }

    public String getFormatValue() {
        return element.getValueDescription(rawValue);
    }

    public ElementSSIM getElement() {
        return element;
    }
}
