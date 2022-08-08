package com.cargo.booking.claim.s3.repository;

import com.cargo.booking.claim.s3.S3Client;
import com.cargo.booking.claim.s3.model.StoredFile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.util.UUID;

@Repository
public class S3RepositoryImpl implements S3Repository<StoredFile, UUID> {
    @PersistenceContext
    private EntityManager entityManager;

    private final S3Client s3Client;

    public S3RepositoryImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public StoredFile save(StoredFile storedFile) {
        throw new UnsupportedOperationException("Need file content input stream");
    }

    @Transactional
    public StoredFile save(StoredFile storedFile, InputStream inputStream) {
        entityManager.persist(storedFile);
        s3Client.uploadFile(storedFile.getId(), inputStream, storedFile.getContentLength());

        return storedFile;
    }

    @Transactional
    public void delete(StoredFile storedFile) {
        s3Client.deleteFile(storedFile.getId());
        entityManager.remove(storedFile);
    }

    public InputStream downloadContent(StoredFile storedFile) {
        return s3Client.downloadFile(storedFile.getId());
    }
}
