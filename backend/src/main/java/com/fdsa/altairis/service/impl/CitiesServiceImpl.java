package com.fdsa.altairis.service.impl;

import com.fdsa.altairis.dto.request.CityRequest;
import com.fdsa.altairis.dto.response.CityResponse;
import com.fdsa.altairis.exception.ApplicationException;
import com.fdsa.altairis.mapper.CitiesMapper;
import com.fdsa.altairis.model.City;
import com.fdsa.altairis.model.Country;
import com.fdsa.altairis.repository.CitiesRepo;
import com.fdsa.altairis.repository.CountriesRepo;
import com.fdsa.altairis.service.CitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CitiesServiceImpl implements CitiesService {

    private final CitiesRepo citiesRepo;
    private final CountriesRepo countriesRepo;
    private final CitiesMapper citiesMapper;

    @Override
    public CityResponse create(CityRequest request, String user) {

        Country country = countriesRepo.findById(request.getCountryId())
                .orElseThrow(() -> new ApplicationException("NOT_FOUND" , "Pais no encontrado"));

        City city = City.builder()
                .country(country)
                .name(request.getName())
                .status(request.getStatus())
                .createdAt(Instant.now())
                .createdUser(user)
                .updatedAt(Instant.now())
                .updatedUser(user)
                .build();

        return citiesMapper.toResponse(citiesRepo.save(city));
    }

    @Override
    public CityResponse update(Long id, CityRequest request, String user) {
        City city = citiesRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Ciudad no encontrada"));

        Country country = countriesRepo.findById(request.getCountryId())
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Pais no encontrado"));

        city.setCountry(country);
        city.setName(request.getName());
        city.setStatus(request.getStatus());
        city.setUpdatedAt(Instant.now());
        city.setUpdatedUser(user);

        return citiesMapper.toResponse(citiesRepo.save(city));
    }

    @Override
    public CityResponse delete(Long id, String user) {
        City city = citiesRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Ciudad no encontrada"));

        city.setStatus("I");
        city.setUpdatedAt(Instant.now());
        city.setUpdatedUser(user);

        return citiesMapper.toResponse(citiesRepo.save(city));
    }

    @Override
    public CityResponse getById(Long id) {
        City city = citiesRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Ciudad no encontrada"));
        return citiesMapper.toResponse(city);
    }

    @Override
    public Page<CityResponse> list(Long countryId, Pageable pageable) {

        Page<City> page = (countryId == null)
                ? citiesRepo.findAll(pageable)
                : citiesRepo.findByCountry_Id(countryId, pageable);

        return page.map(citiesMapper::toResponse);
    }
}
