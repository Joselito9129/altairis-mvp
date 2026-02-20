package com.fdsa.altairis.mapper;

import com.fdsa.altairis.dto.response.InventoryDayResponse;
import com.fdsa.altairis.model.InventoryDay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryDaysMapper {

    @Mapping(target = "roomTypeId", source = "roomType.id")
    @Mapping(target = "roomTypeCode", source = "roomType.code")
    @Mapping(target = "roomTypeName", source = "roomType.name")
    @Mapping(target = "hotelId", source = "roomType.hotel.id")
    @Mapping(target = "hotelCode", source = "roomType.hotel.code")
    @Mapping(target = "hotelName", source = "roomType.hotel.name")
    InventoryDayResponse toResponse(InventoryDay inv);
}
