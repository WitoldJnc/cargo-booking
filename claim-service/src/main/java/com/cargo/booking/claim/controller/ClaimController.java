package com.cargo.booking.claim.controller;

import com.cargo.booking.claim.dto.ClaimDto;
import com.cargo.booking.claim.dto.ClaimFileDto;
import com.cargo.booking.claim.dto.NewClaimDto;
import com.cargo.booking.claim.s3.S3Client;
import com.cargo.booking.claim.s3.model.StoredFile;
import com.cargo.booking.claim.service.ClaimService;
import com.cargo.booking.messages.rest.RestMessage;
import com.cargo.booking.messages.rest.RestMessageFormatter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/claims")
@ComponentScan("com.cargo.booking.messages")
public final class ClaimController {

    private final ClaimService claimService;
    private final RestMessageFormatter messageFormatter;
    private final S3Client s3Client;

    @ApiOperation(value = "Get claim by id(GUID)", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @GetMapping(value = "/{claimId}", produces = "application/json")
    public ClaimDto getClaim(@PathVariable UUID claimId) {
        return claimService.getClaim(claimId);
    }

    @ApiOperation(value = "Create new claim", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ClaimDto createClaim(@Valid @RequestBody NewClaimDto newClaim) {
        return claimService.createClaim(newClaim);
    }

    @ApiOperation(value = "Delete claim", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @DeleteMapping(value = "/{claimId}", produces = "application/json")
    public RestMessage deleteClaim(@PathVariable UUID claimId) {
        return messageFormatter.restMessage(claimService.deleteClaim(claimId));
    }

    @ApiOperation(value = "Add file to claim", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @PostMapping(value = "/{claimId}/file",
            consumes = "multipart/form-data", produces = "application/json")
    public RestMessage addFile(@PathVariable UUID claimId,
                               @RequestParam("file") MultipartFile file) throws IOException {
        return messageFormatter.restMessage(
                claimService.createStoredFile(
                        claimId, file.getOriginalFilename(), file.getInputStream(), file.getSize())
        );
    }

    @ApiOperation(value = "List of claim files", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @GetMapping(value = "/{claimId}/file", produces = "application/json")
    public List<ClaimFileDto> getFiles(@PathVariable UUID claimId) {
        return claimService.getClaimStoredFiles(claimId);
    }

    @ApiOperation(value = "Download file", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @GetMapping(value = "/file/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID fileId) {
        StoredFile storedFile = claimService.getStoredFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(storedFile.getContentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + storedFile.getName() + "\"")
                .body(new InputStreamResource(s3Client.downloadFile(storedFile.getId())));
    }

    @ApiOperation(value = "Delete file", authorizations = {@Authorization(value = HttpHeaders.AUTHORIZATION)})
    @DeleteMapping(value = "/file/{fileId}", produces = "application/json")
    public RestMessage deleteFile(@PathVariable UUID fileId) {
        return messageFormatter.restMessage(claimService.deleteStoredFile(fileId));
    }
}
