package com.fdsa.altairis.controller;

import com.fdsa.altairis.dto.GenericResponse;
import com.fdsa.altairis.dto.response.DashboardOverviewResponse;
import com.fdsa.altairis.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    public GenericResponse<DashboardOverviewResponse> overview(
            @RequestParam(defaultValue = "30") int days
    ) {
        return GenericResponse.ok(dashboardService.overview(days), "OK");
    }
}

