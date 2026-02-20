package com.fdsa.altairis.service;

import com.fdsa.altairis.dto.request.HotelRequest;
import com.fdsa.altairis.dto.response.HotelResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HotelsService {
    HotelResponse create(HotelRequest request, String user);
    HotelResponse update(Long id, HotelRequest request, String user);
    HotelResponse delete(Long id, String user);
    HotelResponse getById(Long id);
    Page<HotelResponse> search(String q, Long cityId, String status, Pageable pageable);
}
