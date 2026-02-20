package com.fdsa.altairis.controller;

import com.fdsa.altairis.dto.GenericResponse;
import com.fdsa.altairis.dto.request.CityRequest;
import com.fdsa.altairis.dto.response.CityResponse;
import com.fdsa.altairis.service.CitiesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CitiesController {

    private final CitiesService citiesService;

    @PostMapping
    public GenericResponse<CityResponse> create(@Valid @RequestBody CityRequest request) {
        return GenericResponse.ok(citiesService.create(request, "system"), "Ciudad creada");
    }

    @PutMapping("/{id}")
    public GenericResponse<CityResponse> update(@PathVariable Long id, @Valid @RequestBody CityRequest request) {
        return GenericResponse.ok(citiesService.update(id, request, "system"), "Ciudad actualizada");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<CityResponse> delete(@PathVariable Long id) {
        return GenericResponse.ok(citiesService.delete(id, "system"), "Ciudad actualizada");
    }

    @GetMapping("/{id}")
    public GenericResponse<CityResponse> getById(@PathVariable Long id) {
        return GenericResponse.ok(citiesService.getById(id), "OK");
    }

    @GetMapping
    public GenericResponse<Page<CityResponse>> list(@RequestParam(required = false) Long countryId, Pageable pageable) {
        return GenericResponse.ok(citiesService.list(countryId, pageable), "OK");
    }

}
