package com.fdsa.altairis.repository;

import com.fdsa.altairis.model.InventoryDay;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventoryDaysRepo extends JpaRepository<InventoryDay, Long> {
    Optional<InventoryDay> findByRoomType_IdAndDate(Long roomTypeId, LocalDate date);
    List<InventoryDay> findByRoomType_IdAndDateBetween(Long roomTypeId, LocalDate from, LocalDate to);
    List<InventoryDay> findByRoomType_Hotel_IdAndDateBetween(Long hotelId, LocalDate from, LocalDate to);
    boolean existsByRoomType_IdAndDate(Long id, @NotNull LocalDate date);
    //long sumAvailableUnitsByStatusAndDateBetween(String status, LocalDate from, LocalDate to);
   @Query("""
        select coalesce(sum(i.availableUnits), 0)
        from InventoryDay i
        where i.status = :status
          and i.date between :from and :to
    """)
   long sumAvailableUnitsBetween(@Param("status") String status,
                                 @Param("from") LocalDate from,
                                 @Param("to") LocalDate to);
}