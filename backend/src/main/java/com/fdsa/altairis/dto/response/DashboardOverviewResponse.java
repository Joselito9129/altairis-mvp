package com.fdsa.altairis.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class DashboardOverviewResponse {
    private long hotels;
    private long inventory;
    private long bookings;
    private List<String> labels;
    private List<Long> bookingsPerDay;
}
