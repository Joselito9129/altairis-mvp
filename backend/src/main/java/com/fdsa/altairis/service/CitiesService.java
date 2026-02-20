package com.fdsa.altairis.service;

import com.fdsa.altairis.dto.request.CityRequest;
import com.fdsa.altairis.dto.response.CityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CitiesService {

    CityResponse create(CityRequest request, String user);
    CityResponse update(Long id, CityRequest request, String user);
    CityResponse delete(Long id, String user);
    CityResponse getById(Long id);
    Page<CityResponse> list(Long countryId, Pageable pageable);

}
