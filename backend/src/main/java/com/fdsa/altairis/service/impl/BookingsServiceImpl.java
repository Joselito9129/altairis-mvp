package com.fdsa.altairis.service.impl;

import com.fdsa.altairis.dto.request.BookingRequest;
import com.fdsa.altairis.dto.response.BookingNightResponse;
import com.fdsa.altairis.dto.response.BookingResponse;
import com.fdsa.altairis.exception.ApplicationException;
import com.fdsa.altairis.mapper.BookingsMapper;
import com.fdsa.altairis.model.*;
import com.fdsa.altairis.repository.*;
import com.fdsa.altairis.service.BookingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingsServiceImpl implements BookingsService {

    private final BookingsRepo bookingsRepo;
    private final BookingNightsRepo bookingNightsRepo;
    private final HotelsRepo hotelsRepo;
    private final RoomTypesRepo roomTypesRepo;
    private final InventoryDaysRepo inventoryDaysRepo;
    private final BookingsMapper bookingsMapper;

    @Override
    @Transactional
    public BookingResponse create(BookingRequest request, String user) {
        if (!request.getCheckOut().isAfter(request.getCheckIn())) {
            throw new ApplicationException("VALIDATION_ERROR", "checkOut must be after checkIn");
        }

        Hotel hotel = hotelsRepo.findById(request.getHotelId())
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Hotel not found"));

        RoomType rt = roomTypesRepo.findById(request.getRoomTypeId())
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Room type not found"));

        if (!rt.getHotel().getId().equals(hotel.getId())) {
            throw new ApplicationException("VALIDATION_ERROR", "Room type does not belong to hotel");
        }

        // Validate inventory for each night (checkOut excluded)
        LocalDate d = request.getCheckIn();
        while (d.isBefore(request.getCheckOut())) {
            LocalDate finalD = d;
            InventoryDay inv = inventoryDaysRepo.findByRoomType_IdAndDate(rt.getId(), d)
                    .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Inventory not configured for date " + finalD));
            if (inv.getAvailableUnits() < request.getRooms()) {
                throw new ApplicationException("NO_AVAILABILITY", "Not enough availability for date " + d);
            }
            d = d.plusDays(1);
        }

        Booking booking = Booking.builder()
                .bookingRef(generateRef())
                .hotel(hotel)
                .roomType(rt)
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .rooms(request.getRooms())
                .customerName(request.getCustomerName())
                .notes(request.getNotes())
                .status("C") // C=CONFIRMED, X=CANCELLED, P=PENDING (simple)
                .createdAt(Instant.now())
                .createdUser(user)
                .updatedAt(Instant.now())
                .updatedUser(user)
                .build();

        booking = bookingsRepo.save(booking);

        // Apply inventory + create booking nights
        d = request.getCheckIn();
        while (d.isBefore(request.getCheckOut())) {
            InventoryDay inv = inventoryDaysRepo.findByRoomType_IdAndDate(rt.getId(), d).orElseThrow();
            inv.setAvailableUnits(inv.getAvailableUnits() - request.getRooms());
            inv.setUpdatedAt(Instant.now());
            inv.setUpdatedUser(user);
            inventoryDaysRepo.save(inv);

            BookingNight night = BookingNight.builder()
                    .booking(booking)
                    .roomType(rt)
                    .date(d)
                    .rooms(request.getRooms())
                    .build();
            bookingNightsRepo.save(night);

            d = d.plusDays(1);
        }

        return bookingsMapper.toResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse cancel(Long bookingId, String user) {
        Booking booking = bookingsRepo.findById(bookingId)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Booking not found"));

        if ("X".equals(booking.getStatus())) {
            return bookingsMapper.toResponse(booking);
        }

        List<BookingNight> nights = bookingNightsRepo.findByBooking_Id(bookingId);

        // Restore inventory
        for (BookingNight n : nights) {
            InventoryDay inv = inventoryDaysRepo.findByRoomType_IdAndDate(n.getRoomType().getId(), n.getDate())
                    .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Inventory not configured for date " + n.getDate()));
            inv.setAvailableUnits(inv.getAvailableUnits() + n.getRooms());
            if (inv.getAvailableUnits() > inv.getTotalUnits()) {
                inv.setAvailableUnits(inv.getTotalUnits());
            }
            inv.setUpdatedAt(Instant.now());
            inv.setUpdatedUser(user);
            inventoryDaysRepo.save(inv);
        }

        booking.setStatus("X");
        booking.setUpdatedAt(Instant.now());
        booking.setUpdatedUser(user);
        booking = bookingsRepo.save(booking);

        return bookingsMapper.toResponse(booking);
    }

    @Override
    public BookingResponse getById(Long bookingId) {
        Booking booking = bookingsRepo.findById(bookingId)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Booking not found"));
        return bookingsMapper.toResponse(booking);
    }

    @Override
    public Page<BookingResponse> list(Long hotelId, String status, LocalDate from, LocalDate to, org.springframework.data.domain.Pageable pageable) {
        if (hotelId == null) {
            throw new ApplicationException("VALIDATION_ERROR", "hotelId is required");
        }
        if (from == null || to == null) {
            throw new ApplicationException("VALIDATION_ERROR", "from/to are required");
        }

        var page = (status == null || status.isBlank())
                ? bookingsRepo.findByHotel_IdAndCheckInBetween(hotelId, from, to, pageable)
                : bookingsRepo.findByHotel_IdAndStatusAndCheckInBetween(hotelId, status, from, to, pageable);

        return page.map(bookingsMapper::toResponse);
    }

    @Override
    public List<BookingNightResponse> nights(Long bookingId) {
        return bookingNightsRepo.findByBooking_Id(bookingId)
                .stream()
                .map(n -> BookingNightResponse.builder()
                        .id(n.getId())
                        .bookingId(n.getBooking().getId())
                        .date(n.getDate())
                        .rooms(n.getRooms())
                        .build())
                .toList();
    }

    private String generateRef() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String rand = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "ALT-" + date + "-" + rand;
    }
}

