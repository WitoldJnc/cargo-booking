package com.cargo.booking.nsi.dto;

import com.cargo.booking.nsi.model.ColumnType;
import com.cargo.booking.nsi.model.DictionaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XlsxHeader {
    private String fieldName;
    private int xlsxColumnIndex;
    private ColumnType columnType;
    private Boolean isRequired;
    private DictionaryType dictionaryLink;
}