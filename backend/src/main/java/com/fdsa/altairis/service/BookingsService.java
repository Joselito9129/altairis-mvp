package com.fdsa.altairis.service;

import com.fdsa.altairis.dto.request.BookingRequest;
import com.fdsa.altairis.dto.response.BookingNightResponse;
import com.fdsa.altairis.dto.response.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface BookingsService {
    BookingResponse create(BookingRequest request, String user);
    BookingResponse cancel(Long bookingId, String user);
    BookingResponse getById(Long bookingId);
    Page<BookingResponse> list(Long hotelId, String status, LocalDate from, LocalDate to, Pageable pageable);
    List<BookingNightResponse> nights(Long bookingId);
}

