package com.cargo.booking.nsi.model;

import com.cargo.booking.messages.ServiceException;
import com.cargo.booking.messages.ServiceMessage;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.UUID;

@Slf4j
@Getter
@Setter
@Entity
@Table(name = "airport", schema = "nsi")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Airport {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country countryId;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City cityId;

    @Column(nullable = false)
    private String code;

    @Type(type = "jsonb")
    @Column(nullable = false, columnDefinition = "jsonb")
    private JsonLocale name;

    @Transient
    private String nameLocale;

    @PostLoad
    public void setNameLocale() {
        try {
            Field field = name.getClass().getDeclaredField(LocaleContextHolder.getLocale().getLanguage());
            this.nameLocale = (String) field.get(name);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(new ServiceMessage("nsi.error_get_locale", "airport.name", this.code));
        }
    }
}
