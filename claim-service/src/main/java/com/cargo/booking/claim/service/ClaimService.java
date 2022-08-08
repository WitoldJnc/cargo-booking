package com.cargo.booking.claim.service;

import com.cargo.booking.claim.dto.ClaimDto;
import com.cargo.booking.claim.dto.ClaimFileDto;
import com.cargo.booking.claim.dto.NewClaimDto;
import com.cargo.booking.claim.mapper.ClaimMapper;
import com.cargo.booking.claim.model.Claim;
import com.cargo.booking.claim.repository.ClaimRepository;
import com.cargo.booking.claim.s3.model.StoredFile;
import com.cargo.booking.claim.s3.repository.StoredFileRepository;
import com.cargo.booking.messages.ServiceException;
import com.cargo.booking.messages.ServiceMessage;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@ComponentScan("com.cargo.booking.messages")
public final class ClaimService {

    private final ClaimRepository claimRepository;
    private final ClaimMapper claimMapper;
    private final StoredFileRepository storedFileRepository;
    // messageSource должны использовать классы которые занимаются форматированием и шаблонизацией конечных сообщений
    // сейчас такого класса нет, поэтому оно здесь. Временно
    private final MessageSource messageSource;
    private final NotificationService notificationService;

    public ClaimDto getClaim(UUID claimId) {
        Optional<Claim> claim = claimRepository.findById(claimId);

        return claim.map(claimMapper::claimToClaimDto)
                .orElseThrow(() -> new ServiceException(new ServiceMessage("claim.not_found", claimId.toString())));
    }

    public ClaimDto createClaim(NewClaimDto newClaimDto) {
        Claim newClaim = claimMapper.newClaimDtoToClaim(newClaimDto);
        newClaim = claimRepository.save(newClaim);

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Locale currentLocale = LocaleContextHolder.getLocale();
        notificationService.mailTo(
                username,
                messageSource.getMessage("claim.created_subject", null, currentLocale),
                messageSource.getMessage("claim.created", new String[]{newClaim.getName()}, currentLocale)
        );

        return claimMapper.claimToClaimDto(newClaim);
    }

    public ServiceMessage deleteClaim(UUID claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ServiceException(new ServiceMessage("claim.not_found", claimId.toString())));
        for (StoredFile storedFile : claim.getStoredFiles()) {
            storedFileRepository.delete(storedFile);
        }
        claimRepository.delete(claim);

        return new ServiceMessage("claim.deleted", claim.getName());
    }

    public ServiceMessage createStoredFile(UUID claimId, String fileName, InputStream inputStream, long contentLength) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ServiceException(new ServiceMessage("claim.not_found", claimId.toString())));

        StoredFile storedFile = new StoredFile();
        storedFile.setName(fileName);
        storedFile.setContentLength(contentLength);

        storedFileRepository.save(storedFile, inputStream);
        claim.getStoredFiles().add(storedFile);
        claimRepository.save(claim);

        return new ServiceMessage("claim.file_uploaded");
    }

    public List<ClaimFileDto> getClaimStoredFiles(UUID claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ServiceException(new ServiceMessage("claim.not_found", claimId.toString())));

        return claimMapper.storedFilesToClaimFileDto(claim.getStoredFiles());
    }

    public StoredFile getStoredFile(UUID fileId) {
        return storedFileRepository.findById(fileId)
                .orElseThrow(() -> new ServiceException(new ServiceMessage("file.not_found", fileId.toString())));
    }

    public ServiceMessage deleteStoredFile(UUID fileId) {
        StoredFile storedFile = getStoredFile(fileId);
        storedFileRepository.delete(storedFile);

        return new ServiceMessage("file.deleted", storedFile.getName());
    }
}
