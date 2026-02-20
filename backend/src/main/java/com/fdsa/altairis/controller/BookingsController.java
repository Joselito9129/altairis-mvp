package com.fdsa.altairis.controller;

import com.fdsa.altairis.dto.GenericResponse;
import com.fdsa.altairis.dto.request.BookingRequest;
import com.fdsa.altairis.dto.response.BookingNightResponse;
import com.fdsa.altairis.dto.response.BookingResponse;
import com.fdsa.altairis.service.BookingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingsController {

    private final BookingsService bookingsService;

    @PostMapping
    public GenericResponse<BookingResponse> create(@Valid @RequestBody BookingRequest request) {
        return GenericResponse.ok(bookingsService.create(request, "system"), "Booking created");
    }

    @PostMapping("/{id}/cancel")
    public GenericResponse<BookingResponse> cancel(@PathVariable Long id) {
        return GenericResponse.ok(bookingsService.cancel(id, "system"), "Booking cancelled");
    }

    @GetMapping("/{id}")
    public GenericResponse<BookingResponse> getById(@PathVariable Long id) {
        return GenericResponse.ok(bookingsService.getById(id), "OK");
    }

    @GetMapping
    public GenericResponse<Page<BookingResponse>> list(
            @RequestParam Long hotelId,
            @RequestParam(required = false) String status,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            Pageable pageable
    ) {
        return GenericResponse.ok(bookingsService.list(hotelId, status, from, to, pageable), "OK");
    }

    @GetMapping("/{id}/nights")
    public GenericResponse<List<BookingNightResponse>> nights(@PathVariable Long id) {
        return GenericResponse.ok(bookingsService.nights(id), "OK");
    }
}

