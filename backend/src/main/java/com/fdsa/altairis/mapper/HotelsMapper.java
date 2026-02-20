package com.fdsa.altairis.mapper;


import com.fdsa.altairis.dto.response.HotelResponse;
import com.fdsa.altairis.model.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HotelsMapper {
    @Mapping(target = "cityId", source = "city.id")
    @Mapping(target = "cityName", source = "city.name")
    @Mapping(target = "countryId", source = "city.country.id")
    @Mapping(target = "countryIso2", source = "city.country.iso2")
    @Mapping(target = "countryName", source = "city.country.name")
    HotelResponse toResponse(Hotel hotel);
}
