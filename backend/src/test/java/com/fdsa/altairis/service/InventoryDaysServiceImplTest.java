package com.fdsa.altairis.service;

import com.fdsa.altairis.dto.request.InventoryDayRequest;
import com.fdsa.altairis.exception.ApplicationException;
import com.fdsa.altairis.mapper.InventoryDaysMapper;
import com.fdsa.altairis.model.RoomType;
import com.fdsa.altairis.repository.InventoryDaysRepo;
import com.fdsa.altairis.repository.RoomTypesRepo;
import com.fdsa.altairis.service.impl.InventoryDaysServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryDaysServiceImplTest {
    @InjectMocks
    private InventoryDaysServiceImpl service;

    @Mock private InventoryDaysRepo inventoryDaysRepo;
    @Mock private RoomTypesRepo roomTypesRepo;
    @Mock
    private InventoryDaysMapper inventoryDaysMapper;

    @Test
    void create_shouldThrow_whenAvailableGreaterThanTotal() {
        InventoryDayRequest req = new InventoryDayRequest();
        req.setRoomTypeId(1L);
        req.setDate(LocalDate.now());
        req.setTotalUnits(5);
        req.setAvailableUnits(10); // invalid
        req.setCurrency("EUR");
        req.setStatus("A");

        ApplicationException ex = assertThrows(ApplicationException.class,
                () -> service.create(req, "test"));

        assertEquals("VALIDATION_ERROR", ex.getCode());
    }

    @Test
    void create_shouldThrow_whenDuplicateRoomTypeAndDate() {
        InventoryDayRequest req = new InventoryDayRequest();
        req.setRoomTypeId(1L);
        req.setDate(LocalDate.now());
        req.setTotalUnits(5);
        req.setAvailableUnits(5);
        req.setCurrency("EUR");
        req.setStatus("A");

        RoomType rt = new RoomType();
        rt.setId(1L);

        when(roomTypesRepo.findById(1L)).thenReturn(Optional.of(rt));
        when(inventoryDaysRepo.existsByRoomType_IdAndDate(1L, req.getDate())).thenReturn(true);

        ApplicationException ex = assertThrows(ApplicationException.class,
                () -> service.create(req, "test"));

        assertEquals("CONFLICT", ex.getCode());
    }
}
