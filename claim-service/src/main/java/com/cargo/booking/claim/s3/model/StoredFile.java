package com.cargo.booking.claim.s3.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "stored_file")
public class StoredFile {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private Long contentLength;
}
