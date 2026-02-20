package com.fdsa.altairis.service;

import com.fdsa.altairis.dto.request.CountryRequest;
import com.fdsa.altairis.dto.response.CountryResponse;
import org.springframework.data.domain.Page;

public interface CountriesService {
    Page<CountryResponse> search(String q, String status, int page, int size);

    CountryResponse getById(Long id);

    CountryResponse create(CountryRequest request, String user);

    CountryResponse update(Long id, CountryRequest request, String user);

    CountryResponse delete(Long id, String user);
}
