package com.fdsa.altairis.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class BookingResponse {
    private Long id;
    private String bookingRef;

    private Long hotelId;
    private String hotelCode;
    private String hotelName;

    private Long roomTypeId;
    private String roomTypeCode;
    private String roomTypeName;

    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer rooms;

    private String customerName;
    private String notes;

    private String status;
}
