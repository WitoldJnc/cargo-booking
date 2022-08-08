package com.cargo.booking.account.dto.company;

import lombok.Data;

import java.util.UUID;

@Data
public class CompanyDto {
    private UUID id;
    private String shortName;
    private String fullName;
    private String inn;
    private String kpp;
    private String ogrn;
    private String postAddress;
    private String legalAddress;
    private String description;
    private UUID countryId;
    private String contactPerson;
    private String position;
    private String phone;
    private String email;
    private String logo;
}
