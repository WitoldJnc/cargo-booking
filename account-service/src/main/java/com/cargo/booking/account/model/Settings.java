package com.cargo.booking.account.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "settings")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Settings {

    @Id
    @GeneratedValue
    private UUID id;

    @Type(type = "jsonb")
    @Column(nullable = false, columnDefinition = "jsonb")
    private JsonSettings value;
}
