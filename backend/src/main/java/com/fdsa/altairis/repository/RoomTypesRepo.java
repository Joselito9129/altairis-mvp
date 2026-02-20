package com.fdsa.altairis.repository;

import com.fdsa.altairis.model.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomTypesRepo extends JpaRepository<RoomType, Long> {
    Optional<RoomType> findByHotel_IdAndCode(Long hotelId, String code);
    Page<RoomType> findByHotel_Id(Long hotelId, Pageable pageable);
}
