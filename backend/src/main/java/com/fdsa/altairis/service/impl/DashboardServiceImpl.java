package com.fdsa.altairis.service.impl;

import com.fdsa.altairis.dto.response.DashboardOverviewResponse;
import com.fdsa.altairis.repository.BookingsRepo;
import com.fdsa.altairis.repository.HotelsRepo;
import com.fdsa.altairis.repository.InventoryDaysRepo;
import com.fdsa.altairis.repository.projection.DateCountProjection;
import com.fdsa.altairis.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final HotelsRepo hotelsRepo;
    private final InventoryDaysRepo inventoryDaysRepo;
    private final BookingsRepo bookingsRepo;

    @Override
    public DashboardOverviewResponse overview(int days) {
        int safeDays = Math.max(1, Math.min(days, 365));

        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(safeDays - 1L);

        long hotels = hotelsRepo.countByStatus("A");
        long inventory = inventoryDaysRepo.sumAvailableUnitsBetween("A", from, to);
        long bookings = bookingsRepo.countByStatus("A"); // o status que uses para activas

        // Serie completa: labels + valores, llenando con 0
        Map<LocalDate, Long> perDay = new HashMap<>();
        List<DateCountProjection> rows = bookingsRepo.findBookingsCountPerDay("A", from, to);
        for (DateCountProjection r : rows) {
            perDay.put(r.getCheckIn(), r.getCount() == null ? 0L : r.getCount());
        }

        List<String> labels = new ArrayList<>(safeDays);
        List<Long> values = new ArrayList<>(safeDays);

        for (int i = 0; i < safeDays; i++) {
            LocalDate d = from.plusDays(i);
            labels.add(d.toString());
            values.add(perDay.getOrDefault(d, 0L));
        }

        return DashboardOverviewResponse.builder()
                .hotels(hotels)
                .inventory(inventory)
                .bookings(bookings)
                .labels(labels)
                .bookingsPerDay(values)
                .build();
    }
}
