package com.fdsa.altairis.service;

import com.fdsa.altairis.dto.response.DashboardOverviewResponse;

public interface DashboardService {
    DashboardOverviewResponse overview(int days);
}
