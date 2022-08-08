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
@Table(name = "workspace_role")
public class WorkspaceRole {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "workspace_role_authority",
            joinColumns = @JoinColumn(name = "workspace_role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WorkspaceRole that = (WorkspaceRole) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
