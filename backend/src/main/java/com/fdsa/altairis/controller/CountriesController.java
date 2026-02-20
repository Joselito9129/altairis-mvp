package com.fdsa.altairis.controller;

import com.fdsa.altairis.dto.GenericResponse;
import com.fdsa.altairis.dto.request.CountryRequest;
import com.fdsa.altairis.dto.response.CountryResponse;
import com.fdsa.altairis.service.CountriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountriesController {

    private final CountriesService countriesService;

    @GetMapping("/search")
    public GenericResponse<Page<CountryResponse>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return GenericResponse.ok(countriesService.search(q, status, page, size), "OK");
    }

    @GetMapping("/{id}")
    public GenericResponse<CountryResponse> getById(@PathVariable Long id) {
        return GenericResponse.ok(countriesService.getById(id), "OK");
    }

    @PostMapping
    public GenericResponse<CountryResponse> create(@Valid @RequestBody CountryRequest request) {
        return GenericResponse.ok(countriesService.create(request, "system"), "Country created");
    }

    @PutMapping("/{id}")
    public GenericResponse<CountryResponse> update(@PathVariable Long id, @Valid @RequestBody CountryRequest request) {
        return GenericResponse.ok(countriesService.update(id, request, "system"), "Country updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<CountryResponse> delete(@PathVariable Long id) {
        return GenericResponse.ok(countriesService.delete(id, "system"), "Country updated");
    }
}

