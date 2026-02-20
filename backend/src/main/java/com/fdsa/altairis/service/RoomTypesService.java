package com.fdsa.altairis.service;

import com.fdsa.altairis.dto.request.RoomTypeRequest;
import com.fdsa.altairis.dto.response.RoomTypeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomTypesService {
    RoomTypeResponse create(RoomTypeRequest request, String user);
    RoomTypeResponse update(Long id, RoomTypeRequest request, String user);
    RoomTypeResponse delete(Long id, String user);
    RoomTypeResponse getById(Long id);
    Page<RoomTypeResponse> listByHotel(Long hotelId, Pageable pageable);
}
