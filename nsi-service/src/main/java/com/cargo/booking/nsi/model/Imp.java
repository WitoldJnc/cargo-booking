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

@Getter
@Setter
@Entity
@Table(name = "imp", schema = "nsi")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Slf4j
public class Imp {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String code;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
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
            throw new ServiceException(new ServiceMessage("nsi.error_get_locale", "shc.name", this.id));
        }
    }
}