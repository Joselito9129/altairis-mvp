package com.fdsa.altairis.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class InventoryDayResponse {

    private Long id;
    private Long roomTypeId;
    private String roomTypeCode;
    private String roomTypeName;

    private Long hotelId;
    private String hotelCode;
    private String hotelName;

    private LocalDate date;
    private Integer totalUnits;
    private Integer availableUnits;
    private BigDecimal price;
    private String currency;
    private String status;

}
