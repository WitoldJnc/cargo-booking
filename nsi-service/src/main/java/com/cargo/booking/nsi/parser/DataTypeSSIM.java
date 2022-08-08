package com.cargo.booking.nsi.parser;

import java.util.regex.Pattern;

public enum DataTypeSSIM {
    f       ("[\\u0020-\\u007E]+"),
    N       ("[0-9]+"),
    N_      ("[0-9 ]+"),
    N_tz    ("[0-9+]+"),
    a       ("[A-Z]+"),
    NNNN    ("[0-9]{4}"),
    ANY     (".*");

    private final Pattern typeRegex;

    DataTypeSSIM(String typeRegex) {
        this.typeRegex = Pattern.compile(typeRegex);
    }

    public boolean isValid(CharSequence s) {
        return typeRegex.matcher(s).matches();
    }

    @Override
    public String toString() {
        return String.format("%s('%s')", name(), typeRegex);
    }
}
