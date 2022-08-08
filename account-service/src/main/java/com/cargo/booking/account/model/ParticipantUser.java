package com.cargo.booking.account.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "participant_user")
public class ParticipantUser {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @Column(name = "administrator")
    private Boolean administrator;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "participant_user_workspace_role",
            joinColumns = @JoinColumn(name = "participant_user_id"),
            inverseJoinColumns = @JoinColumn(name = "workspace_role_id"))
    private Set<WorkspaceRole> workspaceRoles = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ParticipantUser that = (ParticipantUser) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
