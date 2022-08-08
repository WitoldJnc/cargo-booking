package com.cargo.booking.nsi.parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.ValidationException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParserIataSsim {
    private static final int MAX_IATA_CODE_LENGTH = 200;

    public static Value parse(String line, ElementSSIM element) {
        return parse(line, element.getFrom(), element.getTo(), element);
    }

    private static Value parse(String line, int from, int to, ElementSSIM element) {
        assertLength(line);
        String lineValue = line.substring(from, to);

        if (!element.getDataType().isValid(lineValue)) {
            throw new ValidationException();
        }

        return new Value(lineValue, element);
    }

    private static void assertLength(CharSequence s) {
        if (s.length() > MAX_IATA_CODE_LENGTH) {
            throw new ValidationException();
        }
    }
}
