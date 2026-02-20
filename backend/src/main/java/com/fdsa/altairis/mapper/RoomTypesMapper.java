package com.fdsa.altairis.mapper;


import com.fdsa.altairis.dto.response.RoomTypeResponse;
import com.fdsa.altairis.model.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomTypesMapper {
    @Mapping(target = "hotelId", source = "hotel.id")
    @Mapping(target = "hotelCode", source = "hotel.code")
    @Mapping(target = "hotelName", source = "hotel.name")
    RoomTypeResponse toResponse(RoomType roomType);
}
