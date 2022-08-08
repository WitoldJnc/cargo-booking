package com.cargo.booking.nsi.parser;

import java.util.regex.Pattern;

import static com.cargo.booking.nsi.parser.DataTypeSSIM.*;
import static com.cargo.booking.nsi.parser.ValueFormatters.checkChar;
import static com.cargo.booking.nsi.parser.ValueFormatters.trimSpace;

public enum ElementSSIM {
    RECORD_TYPE_NUMBER      (0, 1, f, "Record Type number"),
    OPERATIONAL_SUFFIX      (1, 2, f, "Operational Suffix", checkChar()),
    AIRLINE_CODE            (2, 5, f, "Airline Designator"),
    FLIGHT_NUMBER           (5, 9, NNNN, "Flight Number"),
    IVI                     (9, 11, N, "Itinerary Variation Identifier"),
    LEG_SEQ_NUMBER          (11, 13, N, "Leg Sequence Number"),
    SERVICE_TYPE            (13, 14, a, "Service Type"),
    PERIOD_FROM             (14, 21, f, "Period of Operation from date"),
    PERIOD_TO               (21, 28, f, "Period of Operation to date"),
    DAYS_OF_OPERATION       (28, 35, N_, "Day(s) of Operation", trimSpace()),
    DEPARTURE_STATION       (36, 39, a, "Departure Station"),
    AIRCRAFT_STD            (43, 52, N_tz,"Scheduled Time of Aircraft Departure with time zone"),
    ARRIVAL_STATION         (54, 57, a, "Arrival Station"),
    AIRCRAFT_STA            (61, 70, N_tz, "Scheduled Time of Aircraft Arrival with time zone"),
    AIRCRAFT_IATA_TYPE      (72, 75, f,"Aircraft Type"),
    LINE_NUMBER             (194, 200, N,"Line number");

    private final int from;
    private final int to;
    private final String description;
    private final ValueFormatter valueDescription;
    private final DataTypeSSIM dataType;
    private final Pattern regexPattern;

    ElementSSIM(int from, int to, DataTypeSSIM dataType, String description) {
        this(from, to, dataType, null, description, null);
    }

    ElementSSIM(int from, int to, DataTypeSSIM dataType, String description, ValueFormatter valueDescription) {
        this(from, to, dataType, null, description, valueDescription);
    }

    ElementSSIM(int from, int to, DataTypeSSIM dataType, String regexPattern, String description, ValueFormatter valueDescription) {
        this.from = from;
        this.to = to;
        this.description = description;
        this.valueDescription = valueDescription;
        this.dataType = dataType;
        this.regexPattern = regexPattern != null ? Pattern.compile(regexPattern) : null;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public String getDescription() {
        return description;
    }

    public String getValueDescription(CharSequence value) {
        return valueDescription != null ? valueDescription.getFormattedValue(this, value) : null;
    }

    public DataTypeSSIM getDataType() {
        return dataType;
    }

    public boolean isDataValid(CharSequence sequence) {
        return regexPattern == null || regexPattern.matcher(sequence).matches();
    }
}
