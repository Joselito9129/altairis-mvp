package com.fdsa.altairis.controller;

import com.fdsa.altairis.dto.GenericResponse;
import com.fdsa.altairis.dto.request.HotelRequest;
import com.fdsa.altairis.dto.response.HotelResponse;
import com.fdsa.altairis.service.HotelsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelsController {

    private final HotelsService hotelsService;

    @PostMapping
    public GenericResponse<HotelResponse> create(@Valid @RequestBody HotelRequest request) {
        return GenericResponse.ok(hotelsService.create(request, "system"), "Hotel creado");
    }

    @PutMapping("/{id}")
    public GenericResponse<HotelResponse> update(@PathVariable Long id, @Valid @RequestBody HotelRequest request) {
        return GenericResponse.ok(hotelsService.update(id, request, "system"), "Hotel actualizado");
    }

    @GetMapping("/{id}")
    public GenericResponse<HotelResponse> getById(@PathVariable Long id) {
        return GenericResponse.ok(hotelsService.getById(id), "OK");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<HotelResponse> delete(@PathVariable Long id) {
        return GenericResponse.ok(hotelsService.delete(id, "system"), "Hotel actualizado");
    }

    @GetMapping
    public GenericResponse<Page<HotelResponse>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long cityId,
            @RequestParam(required = false) String status,
            Pageable pageable
    ) {
        return GenericResponse.ok(hotelsService.search(q, cityId, status, pageable), "OK");
    }

}
