package com.fdsa.altairis.service.impl;

import com.fdsa.altairis.dto.request.InventoryDayRequest;
import com.fdsa.altairis.dto.response.InventoryDayResponse;
import com.fdsa.altairis.exception.ApplicationException;
import com.fdsa.altairis.mapper.InventoryDaysMapper;
import com.fdsa.altairis.model.InventoryDay;
import com.fdsa.altairis.model.RoomType;
import com.fdsa.altairis.repository.InventoryDaysRepo;
import com.fdsa.altairis.repository.RoomTypesRepo;
import com.fdsa.altairis.service.InventoryDaysService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryDaysServiceImpl implements InventoryDaysService {

    private final InventoryDaysRepo inventoryDaysRepo;
    private final RoomTypesRepo roomTypesRepo;
    private final InventoryDaysMapper inventoryDaysMapper;

    @Override
    public InventoryDayResponse create(InventoryDayRequest request, String user) {
        if (request.getAvailableUnits() > request.getTotalUnits()) {
            throw new ApplicationException("VALIDATION_ERROR", "Las unidades disponibles no pueden exceder las unidades totales");
        }

        RoomType rt = roomTypesRepo.findById(request.getRoomTypeId())
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Tipo de habitación no encontrado"));

        if (inventoryDaysRepo.existsByRoomType_IdAndDate(rt.getId(), request.getDate())) {
            throw new ApplicationException("CONFLICT", "Ya existe un inventario para este tipo de habitación y fecha");
        }

        InventoryDay inv = InventoryDay.builder()
                .roomType(rt)
                .date(request.getDate())
                .totalUnits(request.getTotalUnits())
                .availableUnits(request.getAvailableUnits())
                .price(request.getPrice())
                .currency(request.getCurrency())
                .status(request.getStatus())
                .createdAt(Instant.now())
                .createdUser(user)
                .updatedAt(Instant.now())   // o null, según tu política
                .updatedUser(user)
                .build();

        InventoryDay saved = inventoryDaysRepo.save(inv);
        return inventoryDaysMapper.toResponse(saved);
    }


    @Override
    public InventoryDayResponse update(Long id, InventoryDayRequest request, String user) {
        if (request.getAvailableUnits() > request.getTotalUnits()) {
            throw new ApplicationException("VALIDATION_ERROR", "Las unidades disponibles no pueden exceder las unidades totales");
        }

        RoomType rt = roomTypesRepo.findById(request.getRoomTypeId())
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Tipo de habitación no encontrado"));

        InventoryDay inv = inventoryDaysRepo.findByRoomType_IdAndDate(rt.getId(), request.getDate())
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "No existe inventario para este tipo de habitación y fecha"));

        inv.setTotalUnits(request.getTotalUnits());
        inv.setAvailableUnits(request.getAvailableUnits());
        inv.setPrice(request.getPrice());
        inv.setCurrency(request.getCurrency());
        inv.setStatus(request.getStatus());
        inv.setUpdatedAt(Instant.now());
        inv.setUpdatedUser(user);

        InventoryDay saved = inventoryDaysRepo.save(inv);
        return inventoryDaysMapper.toResponse(saved);
    }

    @Override
    public InventoryDayResponse delete(Long id, String user) {

        InventoryDay inv = inventoryDaysRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "No encontrado"));

        inv.setStatus("I");
        inv.setUpdatedAt(Instant.now());
        inv.setUpdatedUser(user);

        InventoryDay saved = inventoryDaysRepo.save(inv);
        return inventoryDaysMapper.toResponse(saved);
    }

    @Override
    public InventoryDayResponse getById(Long id) {
        InventoryDay rt = inventoryDaysRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "No encontrado"));
        return inventoryDaysMapper.toResponse(rt);
    }


    @Override
    public List<InventoryDayResponse> listByRoomType(Long roomTypeId, LocalDate from, LocalDate to) {
        return inventoryDaysRepo.findByRoomType_IdAndDateBetween(roomTypeId, from, to)
                .stream().map(inventoryDaysMapper::toResponse).toList();
    }

    @Override
    public List<InventoryDayResponse> listByHotel(Long hotelId, LocalDate from, LocalDate to) {
        return inventoryDaysRepo.findByRoomType_Hotel_IdAndDateBetween(hotelId, from, to)
                .stream().map(inventoryDaysMapper::toResponse).toList();
    }
}

