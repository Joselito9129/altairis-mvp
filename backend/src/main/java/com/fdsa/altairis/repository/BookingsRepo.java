package com.fdsa.altairis.repository;

import com.fdsa.altairis.model.Booking;
import com.fdsa.altairis.repository.projection.DateCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingsRepo extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingRef(String bookingRef);

    Page<Booking> findByHotel_IdAndCheckInBetween(Long hotelId, LocalDate from, LocalDate to, Pageable pageable);

    Page<Booking> findByHotel_IdAndStatusAndCheckInBetween(Long hotelId, String status, LocalDate from, LocalDate to, Pageable pageable);

    long countByStatus(String status);

    //List<DateCountProjection> findByStatusAndCheckInBetweenOrderByCheckIn(String status, LocalDate to, LocalDate localDate);
    @Query("""
        SELECT b.checkIn AS checkIn, COUNT(b.id) AS cnt
        FROM Booking b
        WHERE b.status = :status
          AND b.checkIn BETWEEN :from AND :to
        GROUP BY b.checkIn
        ORDER BY b.checkIn
    """)
    List<DateCountProjection> findBookingsCountPerDay(
            @Param("status") String status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
