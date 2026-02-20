package com.fdsa.altairis.service;

import com.fdsa.altairis.dto.request.HotelRequest;
import com.fdsa.altairis.exception.ApplicationException;
import com.fdsa.altairis.mapper.HotelsMapper;
import com.fdsa.altairis.repository.CitiesRepo;
import com.fdsa.altairis.repository.HotelsRepo;
import com.fdsa.altairis.service.impl.HotelsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelsServiceImplTest {

    @InjectMocks
    private HotelsServiceImpl service;

    @Mock private HotelsRepo hotelsRepo;
    @Mock private CitiesRepo citiesRepo;
    @Mock private HotelsMapper hotelsMapper;

    @Test
    void create_shouldThrow_whenCityNotFound() {
        HotelRequest req = new HotelRequest();
        req.setCode("H-001");
        req.setName("Hotel One");
        req.setCityId(99L);
        req.setStatus("A");

        when(citiesRepo.findById(99L)).thenReturn(Optional.empty());

        ApplicationException ex = assertThrows(ApplicationException.class,
                () -> service.create(req, "test"));

        assertEquals("NOT_FOUND", ex.getCode());
    }
}