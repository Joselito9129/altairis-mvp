package com.fdsa.altairis.repository;

import com.fdsa.altairis.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelsRepo extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByCode(String code);
    long countByStatus(String status);
    Page<Hotel> findAll(Specification<Hotel> spec, Pageable pageable);
}
