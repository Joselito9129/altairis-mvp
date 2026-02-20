package com.fdsa.altairis.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
public class BookingNightResponse {
    private Long id;
    private Long bookingId;
    private LocalDate date;
    private Integer rooms;
}
