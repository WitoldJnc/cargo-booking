package com.cargo.booking.claim.model;

import com.cargo.booking.claim.s3.model.StoredFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "claim")
public class Claim {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "claim_file",
            joinColumns = {@JoinColumn(name = "claim_id")},
            inverseJoinColumns = {@JoinColumn(name = "stored_file_id")})
    private Set<StoredFile> storedFiles = new HashSet<>();
}
