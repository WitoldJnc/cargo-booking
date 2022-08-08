package com.cargo.booking.nsi.parser;

public enum RecordTypeSSIM {
    HEADER      ("1"),
    CARRIER     ("2"),
    FLIGHT_LEG  ("3"),
    SEGMENT_DATA("4"),
    TRAILER     ("5");

    private final String recordNumber;

    RecordTypeSSIM(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public static RecordTypeSSIM findByRecordTypeNumber(String recordTypeNumber) {
        for (RecordTypeSSIM r : values()) {
            if (r.getRecordTypeNumber().equals(recordTypeNumber)) {
                return r;
            }
        }
        return null;
    }

    public String getRecordTypeNumber() {
        return recordNumber;
    }
}
