package com.cargo.booking.nsi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "rate", schema = "nsi")
public class Rate {
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Идентификатор участника
     */
    @Column(nullable = false)
    private UUID participantId;

    /**
     * Время загрузки файла
     */
    @Column(nullable = false)
    private LocalDateTime dtUpload;

    /**
     * Флаг активная ли запись
     */
    @Column(nullable = false)
    private Boolean isActive = false;

    /**
     * Наименование загружаемого файла
     */
    private String fileName;

    /**
     * Наименования тарифа
     */
    @Column(nullable = false)
    private String rateName;

    /**
     * Имя перевозчика
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airlineId;

    /**
     * Место отправления
     */
    @ManyToOne
    @JoinColumn(name = "departureId", nullable = false)
    private Airport departureId;

    /**
     * Место прибытия
     */
    @ManyToOne
    @JoinColumn(name = "arrival_id", nullable = false)
    private Airport arrivalId;

    /**
     * Транзитные станции
     */
    private String transitStation;

    /**
     * Коды специальной обработки
     */
    @ManyToOne
    @JoinColumn(name = "imp_id")
    private Imp impId;

    /**
     * Валюта оплаты
     */
    @OneToOne(optional = false)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currencyId;

    /**
     * Минимальный вес
     */
    @Column(nullable = false)
    private Integer minWeight;

    /**
     * До 45кг
     */
    @Column(nullable = false, name = "less_than_45_kg")
    private Integer lessThan45Kg;

    /**
     * От 46 до 100кг
     */
    @Column(nullable = false, name = "from_46_to_100_kg")
    private Integer from46To100Kg;

    /**
     * От 101 до 300кг
     */
    @Column(nullable = false, name = "from_101_to_300_kg")
    private Integer from101To300Kg;

    /**
     * От 301 до 1000кг
     */
    @Column(nullable = false, name = "from_301_to_1000_kg")
    private Integer from301To1000Kg;

    /**
     * Свыше 1001кг
     */
    @Column(nullable = false, name = "over_1001_kg")
    private Integer over1001Kg;

    /**
     * Стоимость авианакладной
     */
    @Column(nullable = false)
    private Integer airbillCost;

    /**
     * Дата начала дейстия тарифа
     */
    @Column(nullable = false)
    private LocalDate rateFrom;

    /**
     * Дата окончания действия тарифа
     */
    @Column(nullable = false)
    private LocalDate rateTo;

    /**
     * Произвольный комментарий
     */
    private String comment;
}