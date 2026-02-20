package com.fdsa.altairis.controller;

import com.fdsa.altairis.dto.GenericResponse;
import com.fdsa.altairis.dto.request.InventoryDayRequest;
import com.fdsa.altairis.dto.response.InventoryDayResponse;
import com.fdsa.altairis.service.InventoryDaysService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/inventory-days")
@RequiredArgsConstructor
public class InventoryDaysController {

    private final InventoryDaysService inventoryDaysService;

    @PostMapping()
    public GenericResponse<InventoryDayResponse> create(@Valid @RequestBody InventoryDayRequest request) {
        return GenericResponse.ok(inventoryDaysService.create(request, "system"), "Inventory updated");
    }

    @PutMapping("/{id}")
    public GenericResponse<InventoryDayResponse> update(@PathVariable Long id, @Valid @RequestBody InventoryDayRequest request) {
        return GenericResponse.ok(inventoryDaysService.update(id, request, "system"), "Inventory updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<InventoryDayResponse> delete(@PathVariable Long id) {
        return GenericResponse.ok(inventoryDaysService.delete(id, "system"), "Inventory updated");
    }

    @GetMapping("/{id}")
    public GenericResponse<InventoryDayResponse> getById(@PathVariable Long id) {
        return GenericResponse.ok(inventoryDaysService.getById(id), "OK");
    }

    @GetMapping("/by-room-type")
    public GenericResponse<List<InventoryDayResponse>> byRoomType(
            @RequestParam Long roomTypeId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        return GenericResponse.ok(inventoryDaysService.listByRoomType(roomTypeId, from, to), "OK");
    }

    @GetMapping("/by-hotel")
    public GenericResponse<List<InventoryDayResponse>> byHotel(
            @RequestParam Long hotelId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        return GenericResponse.ok(inventoryDaysService.listByHotel(hotelId, from, to), "OK");
    }
}
