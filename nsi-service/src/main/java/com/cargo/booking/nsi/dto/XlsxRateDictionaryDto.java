package com.cargo.booking.nsi.dto;

import com.cargo.booking.nsi.model.ColumnType;
import com.cargo.booking.nsi.util.XlsxField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.cargo.booking.nsi.model.DictionaryType.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class XlsxRateDictionaryDto {

    @XlsxField
    private String rateName;

    @XlsxField(pojoColumnType = ColumnType.LINK, dictionaryLink = AIRLINE)
    private String airlineId;

    @XlsxField(pojoColumnType = ColumnType.LINK, dictionaryLink = AIRPORT)
    private String departureId;

    @XlsxField(pojoColumnType = ColumnType.LINK, dictionaryLink = AIRPORT)
    private String arrivalId;

    @XlsxField(required = false)
    private String transitStation;

    @XlsxField(required = false, pojoColumnType = ColumnType.LINK, dictionaryLink = IMP)
    private String impId;

    @XlsxField(pojoColumnType = ColumnType.LINK, dictionaryLink = CURRENCY)
    private String currencyId;

    @XlsxField(pojoColumnType = ColumnType.INTEGER)
    private String minWeight;

    @XlsxField(pojoColumnType = ColumnType.INTEGER)
    private String lessThan45Kg;

    @XlsxField(pojoColumnType = ColumnType.INTEGER)
    private String from46To100Kg;

    @XlsxField(pojoColumnType = ColumnType.INTEGER)
    private String from101To300Kg;

    @XlsxField(pojoColumnType = ColumnType.INTEGER)
    private String from301To1000Kg;

    @XlsxField(pojoColumnType = ColumnType.INTEGER)
    private String over1001Kg;

    @XlsxField(pojoColumnType = ColumnType.INTEGER)
    private String airbillCost;

    @XlsxField(pojoColumnType = ColumnType.DATE)
    private String rateFrom;

    @XlsxField(pojoColumnType = ColumnType.DATE)
    private String rateTo;

    @XlsxField(required = false)
    private String comment;
}