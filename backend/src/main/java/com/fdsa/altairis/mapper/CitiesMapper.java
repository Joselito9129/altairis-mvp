package com.fdsa.altairis.mapper;

import com.fdsa.altairis.dto.response.CityResponse;
import com.fdsa.altairis.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CitiesMapper {

    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "countryIso2", source = "country.iso2")
    @Mapping(target = "countryName", source = "country.name")
    CityResponse toResponse(City city);
}
