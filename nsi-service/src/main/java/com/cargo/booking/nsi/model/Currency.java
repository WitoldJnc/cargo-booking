package com.cargo.booking.nsi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "currency", schema = "nsi")
public class Currency {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String symbol;
}
