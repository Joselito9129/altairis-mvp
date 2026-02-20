package com.fdsa.altairis.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class InventoryDayRequest {

    @NotNull
    private Long roomTypeId;

    @NotNull
    private LocalDate date;

    @NotNull @Min(0)
    private Integer totalUnits;

    @NotNull @Min(0)
    private Integer availableUnits;

    private BigDecimal price;

    @NotBlank @Size(max = 3)
    private String currency;

    @NotBlank
    @Size(max = 1)
    private String status;
}
