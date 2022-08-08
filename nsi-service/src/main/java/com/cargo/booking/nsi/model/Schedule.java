package com.cargo.booking.nsi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "schedule", schema = "nsi")
public class Schedule {
    @Id
    @GeneratedValue
    private UUID id;

    private Character operationalSuffix;

    @ManyToOne(optional = false)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airlineId;

    @Column(nullable = false)
    private String flightNumber;

    @Column(nullable = false)
    private String ivi;

    @Column(nullable = false)
    private String legSequenceNumber;

    @Column(nullable = false)
    private char serviceType;

    @Column(nullable = false)
    private Instant periodOfOperationFrom;

    @Column(nullable = false)
    private Instant periodOfOperationTo;

    @Column(nullable = false)
    private String daysOfOperation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "departure_station", nullable = false)
    private Airport departureStation;

    @Column(nullable = false)
    private Instant departureTimeAircraftTz;

    @ManyToOne(optional = false)
    @JoinColumn(name = "arrival_station", nullable = false)
    private Airport arrivalStation;

    @Column(nullable = false)
    private Instant arrivalTimeAircraft;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraftId;
}
