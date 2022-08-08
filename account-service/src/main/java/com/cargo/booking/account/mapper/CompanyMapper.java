package com.cargo.booking.account.mapper;

import com.cargo.booking.account.dto.company.CompanyDto;
import com.cargo.booking.account.dto.company.NewCompanyDto;
import com.cargo.booking.account.dto.company.ShortCompanyDto;
import com.cargo.booking.account.model.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface CompanyMapper {

    ShortCompanyDto companyToShortCompanyDto(Company company);

    @Mappings(
            @Mapping(target = "logo", ignore = true)
    )
    Company newCompanyDtoToCompany(NewCompanyDto dto);

    CompanyDto companyToCompanyDto(Company company);
}
