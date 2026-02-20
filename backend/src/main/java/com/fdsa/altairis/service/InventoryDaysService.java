package com.fdsa.altairis.service;

import com.fdsa.altairis.dto.request.InventoryDayRequest;
import com.fdsa.altairis.dto.response.InventoryDayResponse;

import java.time.LocalDate;
import java.util.List;

public interface InventoryDaysService {
    InventoryDayResponse create(InventoryDayRequest request, String user);
    InventoryDayResponse update(Long id, InventoryDayRequest request, String user);
    InventoryDayResponse delete(Long id, String user);
    InventoryDayResponse getById(Long id);
    List<InventoryDayResponse> listByRoomType(Long roomTypeId, LocalDate from, LocalDate to);
    List<InventoryDayResponse> listByHotel(Long hotelId, LocalDate from, LocalDate to);
}
