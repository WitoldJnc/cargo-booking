package com.cargo.booking.claim.mapper;

import com.cargo.booking.claim.dto.ClaimDto;
import com.cargo.booking.claim.dto.ClaimFileDto;
import com.cargo.booking.claim.dto.NewClaimDto;
import com.cargo.booking.claim.model.Claim;
import com.cargo.booking.claim.s3.model.StoredFile;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface ClaimMapper {
    ClaimDto claimToClaimDto(Claim claim);

    Claim newClaimDtoToClaim(NewClaimDto claimDto);

    List<ClaimFileDto> storedFilesToClaimFileDto(Set<StoredFile> storedFiles);
}
