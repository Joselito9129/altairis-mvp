package com.fdsa.altairis.repository;

import com.fdsa.altairis.model.BookingNight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingNightsRepo extends JpaRepository<BookingNight, Long> {
    List<BookingNight> findByBooking_Id(Long bookingId);
    void deleteByBooking_Id(Long bookingId);
}

