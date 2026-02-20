package com.fdsa.altairis.service.impl;

import com.fdsa.altairis.dto.request.HotelRequest;
import com.fdsa.altairis.dto.response.HotelResponse;
import com.fdsa.altairis.exception.ApplicationException;
import com.fdsa.altairis.mapper.HotelsMapper;
import com.fdsa.altairis.model.City;
import com.fdsa.altairis.model.Hotel;
import com.fdsa.altairis.repository.CitiesRepo;
import com.fdsa.altairis.repository.HotelsRepo;
import com.fdsa.altairis.repository.spec.HotelsSpec;
import com.fdsa.altairis.service.HotelsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class HotelsServiceImpl implements HotelsService {

    private final HotelsRepo hotelsRepo;
    private final CitiesRepo citiesRepo;
    private final HotelsMapper hotelsMapper;

    @Override
    public HotelResponse create(HotelRequest request, String user) {
        if (hotelsRepo.findByCode(request.getCode()).isPresent()) {
            throw new ApplicationException("DUPLICATE", "Codigo de hotel ya existente");
        }

        City city = citiesRepo.findById(request.getCityId())
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Ciudad no encontrada"));

        Hotel hotel = Hotel.builder()
                .code(request.getCode())
                .name(request.getName())
                .city(city)
                .address(request.getAddress())
                .stars(request.getStars())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .status(request.getStatus())
                .createdAt(Instant.now())
                .createdUser(user)
                .updatedAt(Instant.now())
                .updatedUser(user)
                .build();

        return hotelsMapper.toResponse(hotelsRepo.save(hotel));
    }

    @Override
    public HotelResponse update(Long id, HotelRequest request, String user) {
        Hotel hotel = hotelsRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Hotel no encontrado"));

        City city = citiesRepo.findById(request.getCityId())
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Ciudad no encontrada"));

        hotel.setName(request.getName());
        hotel.setCity(city);
        hotel.setAddress(request.getAddress());
        hotel.setStars(request.getStars());
        hotel.setLatitude(request.getLatitude());
        hotel.setLongitude(request.getLongitude());
        hotel.setStatus(request.getStatus());
        hotel.setUpdatedAt(Instant.now());
        hotel.setUpdatedUser(user);

        return hotelsMapper.toResponse(hotelsRepo.save(hotel));
    }

    public HotelResponse delete(Long id, String user) {
        Hotel hotel = hotelsRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Hotel no encontrado"));

        hotel.setStatus("I");
        hotel.setUpdatedAt(Instant.now());
        hotel.setUpdatedUser(user);

        return hotelsMapper.toResponse(hotelsRepo.save(hotel));
    }

    @Override
    public HotelResponse getById(Long id) {
        Hotel hotel = hotelsRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "Hotel no encontrado"));
        return hotelsMapper.toResponse(hotel);
    }

    @Override
    public Page<HotelResponse> search(String q, Long cityId, String status, Pageable pageable) {
        Specification<Hotel> spec =
                HotelsSpec.statusEq(status)
                        .and(HotelsSpec.cityIdEq(cityId))
                        .and(HotelsSpec.nameLike(q).or(HotelsSpec.codeLike(q)));

    return hotelsRepo.findAll(spec, pageable).map(hotelsMapper::toResponse);
    }

}
