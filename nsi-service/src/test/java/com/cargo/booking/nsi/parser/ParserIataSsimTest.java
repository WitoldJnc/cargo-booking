package com.cargo.booking.nsi.parser;

import org.junit.jupiter.api.Test;

import javax.validation.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ParserIataSsimTest {
    private static final String CORRECT_LINE = "3AS7 10010101J30APR2201MAY22     6  DME03550355+0300  LED05300530+0300  32A                                                                                                 Y174                  000003";
    private static final String WRONG_LINE = "0 S7 10 1 1 1132APP2230APP22  A  6  1ME03550355 0300  1ED05300530 0300  32A                                                                                                 Y174                   00003";
    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("ddMMMyy", Locale.US);
    private static final SimpleDateFormat SDF_TIME = new SimpleDateFormat("HHmmZ", Locale.US);

    @Test
    void testShouldThrowExceptionWhenLineTooLong() {
        assertThrows(ValidationException.class, () -> {
            ParserIataSsim.parse(new String(new char[1000]), ElementSSIM.OPERATIONAL_SUFFIX);
        });
    }

    @Test
    void testRecordTypeNumber() {
        assertEquals(RecordTypeSSIM.FLIGHT_LEG, RecordTypeSSIM.findByRecordTypeNumber(
                ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.RECORD_TYPE_NUMBER).getRawValue()));
    }

    @Test
    void testWrongRecordTypeNumber() {
        assertNull(RecordTypeSSIM.findByRecordTypeNumber(
                ParserIataSsim.parse(WRONG_LINE, ElementSSIM.RECORD_TYPE_NUMBER).getRawValue()));
    }

    @Test
    void testOperationalSuffixPresent() {
        assertEquals("A", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.OPERATIONAL_SUFFIX).getFormatValue());
    }

    @Test
    void testOperationalSuffixEmpty() {
        assertNull(ParserIataSsim.parse(WRONG_LINE, ElementSSIM.OPERATIONAL_SUFFIX).getFormatValue());
    }

    @Test
    void testAirline() {
        assertEquals("S7", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.AIRLINE_CODE).getRawValue());
    }

    @Test
    void testFlitNumber() {
        assertEquals("1001", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.FLIGHT_NUMBER).getRawValue());
    }

    @Test
    void testFlitNumberWrong() {
        assertThrows(ValidationException.class, () -> {
            ParserIataSsim.parse(WRONG_LINE, ElementSSIM.FLIGHT_NUMBER);
        });
    }

    @Test
    void testIVI() {
        assertEquals("01", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.IVI).getRawValue());
    }

    @Test
    void testIVIWrong() {
        assertThrows(ValidationException.class, () -> {
            ParserIataSsim.parse(WRONG_LINE, ElementSSIM.IVI);
        });
    }

    @Test
    void testLeg() {
        assertEquals("01", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.LEG_SEQ_NUMBER).getRawValue());
    }

    @Test
    void testLegWrong() {
        assertThrows(ValidationException.class, () -> {
            ParserIataSsim.parse(WRONG_LINE, ElementSSIM.LEG_SEQ_NUMBER);
        });
    }

    @Test
    void testServiceType() {
        assertEquals("J", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.SERVICE_TYPE).getRawValue());
    }

    @Test
    void testServiceTypeWrong() {
        assertThrows(ValidationException.class, () -> {
            ParserIataSsim.parse(WRONG_LINE, ElementSSIM.SERVICE_TYPE);
        });
    }

    @Test
    void testPeriodFrom() {
        final Instant[] date = {null};
        assertAll(() -> assertDoesNotThrow(() -> assertEquals("30APR22", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.PERIOD_FROM).getRawValue())),
                () -> assertDoesNotThrow(() -> date[0] = SDF_DATE.parse(ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.PERIOD_FROM).getRawValue()).toInstant()),
                () -> assertNotNull(date[0]),
                () -> assertEquals("2022-04-29T19:00:00Z", date[0].toString())
        );
    }

    @Test
    void testPeriodFromWrong() {
        assertThrows(ParseException.class, () -> {
            SDF_DATE.parse(ParserIataSsim.parse(WRONG_LINE, ElementSSIM.PERIOD_FROM).getRawValue()).toInstant();
        });
    }

    @Test
    void testPeriodTo() {
        final Instant[] date = {null};
        assertAll(() -> assertDoesNotThrow(() -> assertEquals("01MAY22", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.PERIOD_TO).getRawValue())),
                () -> date[0] = SDF_DATE.parse(ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.PERIOD_TO).getRawValue()).toInstant(),
                () -> assertNotNull(date[0]),
                () -> assertEquals("2022-04-30T19:00:00Z", date[0].toString())
        );
    }

    @Test
    void testPeriodToWrong() {
        assertThrows(ParseException.class, () -> {
            SDF_DATE.parse(ParserIataSsim.parse(WRONG_LINE, ElementSSIM.PERIOD_TO).getRawValue()).toInstant();
        });
    }

    @Test
    void testDaysOfOperation() {
        assertEquals("6", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.DAYS_OF_OPERATION).getFormatValue());
    }

    @Test
    void testDaysOfOperationWrong() {
        assertThrows(ValidationException.class, () -> {
            ParserIataSsim.parse(WRONG_LINE, ElementSSIM.DAYS_OF_OPERATION);
        });
    }

    @Test
    void testDepartureStation() {
        assertEquals("DME", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.DEPARTURE_STATION).getRawValue());
    }

    @Test
    void testDepartureStationWrong() {
        assertThrows(ValidationException.class, () -> {
            ParserIataSsim.parse(WRONG_LINE, ElementSSIM.DEPARTURE_STATION);
        });
    }

    @Test
    void testAircraftStd() {
        final Instant[] date = {null};
        assertAll(() -> assertEquals("0355+0300", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.AIRCRAFT_STD).getRawValue()),
                () -> assertDoesNotThrow(() -> date[0] = SDF_TIME.parse(ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.AIRCRAFT_STD).getRawValue()).toInstant()),
                () -> assertNotNull(date[0]),
                () -> assertEquals("1970-01-01T00:55:00Z", date[0].toString())
        );
    }

    @Test
    void testAircraftStdWrong() {
        assertThrows(ValidationException.class, () -> {
            ParserIataSsim.parse(WRONG_LINE, ElementSSIM.AIRCRAFT_STD);
        });
    }

    @Test
    void testArrivalStation() {
        assertEquals("LED", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.ARRIVAL_STATION).getRawValue());
    }

    @Test
    void testArrivalStationWrong() {
        assertThrows(ValidationException.class, () -> {
            ParserIataSsim.parse(WRONG_LINE, ElementSSIM.ARRIVAL_STATION);
        });
    }

    @Test
    void testAircraftSta() {
        final Instant[] date = {null};
        assertAll(() -> assertEquals("0530+0300", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.AIRCRAFT_STA).getRawValue()),
                () -> assertDoesNotThrow(() -> date[0] = SDF_TIME.parse(ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.AIRCRAFT_STA).getRawValue()).toInstant()),
                () -> assertNotNull(date[0]),
                () -> assertEquals("1970-01-01T02:30:00Z", date[0].toString())
        );
    }

    @Test
    void testAircraftStaWrong() {
        assertThrows(ValidationException.class, () -> {
            ParserIataSsim.parse(WRONG_LINE, ElementSSIM.AIRCRAFT_STA);
        });
    }

    @Test
    void testAircraft() {
        assertEquals("32A", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.AIRCRAFT_IATA_TYPE).getRawValue());
    }

    @Test
    void testLineNumber() {
        assertEquals("000003", ParserIataSsim.parse(CORRECT_LINE, ElementSSIM.LINE_NUMBER).getRawValue());
    }

    @Test
    void testLineNumberWrong() {
        assertThrows(ValidationException.class, () -> {
            ParserIataSsim.parse(WRONG_LINE, ElementSSIM.LINE_NUMBER);
        });
    }
}