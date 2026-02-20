package com.fdsa.altairis.service.impl;

import com.fdsa.altairis.dto.request.RoomTypeRequest;
import com.fdsa.altairis.dto.response.RoomTypeResponse;
import com.fdsa.altairis.exception.ApplicationException;
import com.fdsa.altairis.mapper.RoomTypesMapper;
import com.fdsa.altairis.model.Hotel;
import com.fdsa.altairis.model.RoomType;
import com.fdsa.altairis.repository.HotelsRepo;
import com.fdsa.altairis.repository.RoomTypesRepo;
import com.fdsa.altairis.service.RoomTypesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RoomTypesServiceImpl implements RoomTypesService {

    private final RoomTypesRepo roomTypesRepo;
    private final HotelsRepo hotelsRepo;
    private final RoomTypesMapper roomTypesMapper;

    @Override
    public RoomTypeResponse create(RoomTypeRequest request, String user) {
        Hotel hotel = hotelsRepo.findById(request.getHotelId())
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Hotel not found"));

        if (roomTypesRepo.findByHotel_IdAndCode(hotel.getId(), request.getCode()).isPresent()) {
            throw new ApplicationException("DUPLICATE", "Room type code already exists for this hotel");
        }

        RoomType rt = RoomType.builder()
                .hotel(hotel)
                .code(request.getCode())
                .name(request.getName())
                .capacityAdults(request.getCapacityAdults())
                .capacityChildren(request.getCapacityChildren())
                .status(request.getStatus())
                .createdAt(Instant.now())
                .createdUser(user)
                .updatedAt(Instant.now())
                .updatedUser(user)
                .build();

        return roomTypesMapper.toResponse(roomTypesRepo.save(rt));
    }

    @Override
    public RoomTypeResponse update(Long id, RoomTypeRequest request, String user) {
        RoomType rt = roomTypesRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Room type not found"));

        rt.setName(request.getName());
        rt.setCapacityAdults(request.getCapacityAdults());
        rt.setCapacityChildren(request.getCapacityChildren());
        rt.setStatus(request.getStatus());
        rt.setUpdatedAt(Instant.now());
        rt.setUpdatedUser(user);

        return roomTypesMapper.toResponse(roomTypesRepo.save(rt));
    }

    @Override
    public RoomTypeResponse delete(Long id, String user) {
        RoomType rt = roomTypesRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Room type not found"));

        rt.setStatus("I");
        rt.setUpdatedAt(Instant.now());
        rt.setUpdatedUser(user);

        return roomTypesMapper.toResponse(roomTypesRepo.save(rt));
    }

    @Override
    public RoomTypeResponse getById(Long id) {
        RoomType rt = roomTypesRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Room type not found"));
        return roomTypesMapper.toResponse(rt);
    }

    @Override
    public Page<RoomTypeResponse> listByHotel(Long hotelId, Pageable pageable) {
        return roomTypesRepo.findByHotel_Id(hotelId, pageable).map(roomTypesMapper::toResponse);
    }
}

