package com.cargo.booking.account.dto.company;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.ru.INN;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class NewCompanyDto {

    @NotBlank(message = "{company.empty_short_name}")
    private String shortName;

    @NotBlank(message = "{company.empty_full_name}")
    private String fullName;

    @INN(message = "{company.invalid_inn}")
    @NotNull(message = "{company.empty_inn}")
    private String inn;

    @Length(min = 9, max = 9, message = "{company.invalid_kpp}")
    @NotNull(message = "{company.empty_kpp}")
    private String kpp;

    @NotBlank(message = "{company.empty_post_address}")
    private String postAddress;

    @NotBlank(message = "{company.empty_legal_address}")
    private String legalAddress;

    private String description;
    private String logo;

    @NotNull(message = "{company.empty_country}")
    private UUID countryId;

    @NotBlank(message = "{company.empty_contact_person}")
    private String contactPerson;

    @NotBlank(message = "{company.empty_position}")
    private String position;

    private String phone;

    @Email(regexp = ".+[@].+[\\.].+", message = "{company.invalid_email}")
    @NotEmpty(message = "{company.empty_email}")
    private String email;
}
