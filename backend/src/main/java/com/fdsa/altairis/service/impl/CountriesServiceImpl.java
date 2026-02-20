package com.fdsa.altairis.service.impl;

import com.fdsa.altairis.dto.request.CountryRequest;
import com.fdsa.altairis.dto.response.CountryResponse;
import com.fdsa.altairis.exception.ApplicationException;
import com.fdsa.altairis.mapper.CountriesMapper;
import com.fdsa.altairis.model.Country;
import com.fdsa.altairis.repository.CountriesRepo;
import com.fdsa.altairis.service.CountriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CountriesServiceImpl implements CountriesService {

    private final CountriesRepo countriesRepo;
    private final CountriesMapper countriesMapper;

    @Override
    public Page<CountryResponse> search(String q, String status, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 200));
        String qq = (q == null || q.isBlank()) ? null : q.trim();
        String st = (status == null || status.isBlank()) ? null : status.trim();

        return countriesRepo.search(qq, st, pageable)
                .map(countriesMapper::toResponse);
    }

    @Override
    public CountryResponse getById(Long id) {
        Country c = countriesRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "País no encontrado"));
        return countriesMapper.toResponse(c);
    }

    @Override
    public CountryResponse create(CountryRequest request, String user) {
        String iso2 = normalizeIso2(request.getIso2());

        if (countriesRepo.existsByIso2IgnoreCase(iso2)) {
            throw new ApplicationException("CONFLICT", "Ya existe un país con ese ISO2");
        }

        Country entity = countriesMapper.toEntity(request);
        entity.setIso2(iso2);

        Instant now = Instant.now();
        entity.setCreatedAt(now);
        entity.setCreatedUser(user);
        entity.setUpdatedAt(now);
        entity.setUpdatedUser(user);

        Country saved = countriesRepo.save(entity);
        return countriesMapper.toResponse(saved);
    }

    @Override
    public CountryResponse update(Long id, CountryRequest request, String user) {
        Country entity = countriesRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "País no encontrado"));

        String iso2 = normalizeIso2(request.getIso2());
        countriesRepo.findByIso2IgnoreCase(iso2).ifPresent(other -> {
            if (!other.getId().equals(id)) {
                throw new ApplicationException("CONFLICT", "Ya existe un país con ese ISO2");
            }
        });

        countriesMapper.updateEntity(entity, request);
        entity.setIso2(iso2);

        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedUser(user);

        Country saved = countriesRepo.save(entity);
        return countriesMapper.toResponse(saved);
    }

    @Override
    public CountryResponse delete(Long id, String user) {
        Country entity = countriesRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "País no encontrado"));

        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedUser(user);
        entity.setStatus("I");

        Country saved = countriesRepo.save(entity);
        return countriesMapper.toResponse(saved);
    }

    private String normalizeIso2(String iso2) {
        if (iso2 == null) return null;
        return iso2.trim().toUpperCase(Locale.ROOT);
    }
}

