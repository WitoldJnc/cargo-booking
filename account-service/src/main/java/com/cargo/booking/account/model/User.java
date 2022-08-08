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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "blocked")
    private Boolean blocked;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<ParticipantUser> participantUsers = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "users_system_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "system_role_id"))
    private Set<SystemRole> systemRoles = new LinkedHashSet<>();
}
