package com.fdsa.altairis.mapper;

import com.fdsa.altairis.dto.response.BookingResponse;
import com.fdsa.altairis.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingsMapper {

    @Mapping(target = "hotelId", source = "hotel.id")
    @Mapping(target = "hotelCode", source = "hotel.code")
    @Mapping(target = "hotelName", source = "hotel.name")
    @Mapping(target = "roomTypeId", source = "roomType.id")
    @Mapping(target = "roomTypeCode", source = "roomType.code")
    @Mapping(target = "roomTypeName", source = "roomType.name")
    BookingResponse toResponse(Booking booking);
}
