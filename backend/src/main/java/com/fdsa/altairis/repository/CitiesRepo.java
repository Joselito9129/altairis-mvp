package com.fdsa.altairis.repository;

import com.fdsa.altairis.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitiesRepo extends JpaRepository<City,Long> {
    Page<City> findByCountry_Id(Long countryId, Pageable pageable);
}
