package com.cargo.booking.account.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "inn", nullable = false)
    private String inn;

    @Column(name = "kpp", nullable = false)
    private String kpp;

    @Column(name = "ogrn", nullable = false)
    private String ogrn;

    @Column(name = "post_address", nullable = false)
    private String postAddress;

    @Column(name = "legal_address", nullable = false)
    private String legalAddress;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "contact_person", nullable = false)
    private String contactPerson;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "country_id", nullable = false)
    private UUID countryId;

    @OneToOne(mappedBy = "company")
    private Participant participant;

    @Column(name = "logo")
    private String logo;
}
