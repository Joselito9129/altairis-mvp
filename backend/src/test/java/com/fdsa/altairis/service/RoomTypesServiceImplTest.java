package com.fdsa.altairis.service;

import com.fdsa.altairis.dto.request.RoomTypeRequest;
import com.fdsa.altairis.exception.ApplicationException;
import com.fdsa.altairis.mapper.RoomTypesMapper;
import com.fdsa.altairis.repository.HotelsRepo;
import com.fdsa.altairis.repository.RoomTypesRepo;
import com.fdsa.altairis.service.impl.RoomTypesServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomTypesServiceImplTest {

    @InjectMocks
    private RoomTypesServiceImpl service;

    @Mock private RoomTypesRepo roomTypesRepo;
    @Mock private HotelsRepo hotelsRepo;
    @Mock private RoomTypesMapper roomTypesMapper;

    @Test
    void create_shouldThrow_whenHotelNotFound() {
        RoomTypeRequest req = new RoomTypeRequest();
        req.setHotelId(10L);
        req.setCode("STD");
        req.setName("Standard");
        req.setStatus("A");

        when(hotelsRepo.findById(10L)).thenReturn(Optional.empty());

        ApplicationException ex = assertThrows(ApplicationException.class,
                () -> service.create(req, "test-user"));

        assertEquals("NOT_FOUND", ex.getCode());
    }
}