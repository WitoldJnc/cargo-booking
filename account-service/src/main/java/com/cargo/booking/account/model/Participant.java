package com.cargo.booking.account.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "participant")
public class Participant {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ParticipantType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ParticipantStatus status;

    @OneToMany(mappedBy = "participant", orphanRemoval = true)
    private Set<ParticipantUser> participantUsers = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "settings_id", nullable = false)
    private Settings settings;
}
