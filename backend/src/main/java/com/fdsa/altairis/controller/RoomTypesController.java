package com.fdsa.altairis.controller;

import com.fdsa.altairis.dto.GenericResponse;
import com.fdsa.altairis.dto.request.RoomTypeRequest;
import com.fdsa.altairis.dto.response.RoomTypeResponse;
import com.fdsa.altairis.service.RoomTypesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypesController {

    private final RoomTypesService roomTypesService;

    @PostMapping
    public GenericResponse<RoomTypeResponse> create(@Valid @RequestBody RoomTypeRequest request) {
        return GenericResponse.ok(roomTypesService.create(request, "system"), "Tipo habitacion creada");
    }

    @PutMapping("/{id}")
    public GenericResponse<RoomTypeResponse> update(@PathVariable Long id, @Valid @RequestBody RoomTypeRequest request) {
        return GenericResponse.ok(roomTypesService.update(id, request, "system"), "Tipo habitacion actualizada");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<RoomTypeResponse> delete(@PathVariable Long id) {
        return GenericResponse.ok(roomTypesService.delete(id, "system"), "Tipo habitacion actualizada");
    }

    @GetMapping("/{id}")
    public GenericResponse<RoomTypeResponse> getById(@PathVariable Long id) {
        return GenericResponse.ok(roomTypesService.getById(id), "OK");
    }

    @GetMapping
    public GenericResponse<Page<RoomTypeResponse>> listByHotel(@RequestParam Long hotelId, Pageable pageable) {
        return GenericResponse.ok(roomTypesService.listByHotel(hotelId, pageable), "OK");
    }
}
