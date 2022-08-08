package com.cargo.booking.claim.s3.repository;

import java.io.InputStream;

public interface S3Repository<E, ID> {
    E save(E entity);

    E save(E entity, InputStream inputStream);

    void delete(E entity);
}
