package com.cargo.booking.nsi.util;

import com.cargo.booking.nsi.model.ColumnType;
import com.cargo.booking.nsi.model.DictionaryType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Важно - порядок полей с аннотацией @XlsxField должен соответствовать порядку полей в .xlsx файле,
 * а наименование полей в dto, помеченные этой аннотацией - наименованию полей в entity
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XlsxField {

    ColumnType pojoColumnType() default ColumnType.STRING;

    boolean required() default true;

    DictionaryType dictionaryLink() default DictionaryType.NONE;

}