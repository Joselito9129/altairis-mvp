package com.fdsa.altairis.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class BookingRequest {

    @NotNull
    private Long hotelId;

    @NotNull
    private Long roomTypeId;

    @NotNull
    private LocalDate checkIn;

    @NotNull
    private LocalDate checkOut;

    @NotNull @Min(1)
    private Integer rooms;

    @NotBlank @Size(max = 160)
    private String customerName;

    @Size(max = 500)
    private String notes;

}
