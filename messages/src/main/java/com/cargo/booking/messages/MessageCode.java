package com.cargo.booking.messages;

public enum MessageCode {
    TOKEN_VALID(0, "auth.token_valid"),
    TOKEN_INVALID(1, "auth.token_invalid");

    private final int intCode;
    private final String stringCode;

    MessageCode(final int intCode, final String stringCode) {
        this.intCode = intCode;
        this.stringCode = stringCode;
    }

    public int getIntCode() {
        return this.intCode;
    }

    public String getStringCode() {
        return this.stringCode;
    }
}
