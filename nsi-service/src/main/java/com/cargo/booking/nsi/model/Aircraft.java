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
@Table(name = "aircraft", schema = "nsi")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Aircraft {
    @Id
    @GeneratedValue
    private UUID id;

    @Type(type = "jsonb")
    @Column(nullable = false, columnDefinition = "jsonb")
    private JsonLocale type;

    @Column(nullable = false)
    private String typeIata;

    @Transient
    private String typeLocale;

    @PostLoad
    public void setNameLocale() {
        try {
            Field field = type.getClass().getDeclaredField(LocaleContextHolder.getLocale().getLanguage());
            this.typeLocale = (String) field.get(type);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(new ServiceMessage("nsi.error_get_locale", "aircraft.type", this.typeIata));
        }
    }
}
