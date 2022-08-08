package com.cargo.booking.claim.s3.repository;

import com.cargo.booking.claim.s3.model.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoredFileRepository extends S3Repository<StoredFile, UUID>, JpaRepository<StoredFile, UUID> {
}
