package com.fdsa.altairis.mapper;

import com.fdsa.altairis.dto.request.CountryRequest;
import com.fdsa.altairis.dto.response.CountryResponse;
import com.fdsa.altairis.model.Country;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CountriesMapper {

    CountryResponse toResponse(Country entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedUser", ignore = true)
    Country toEntity(CountryRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedUser", ignore = true)
    void updateEntity(@MappingTarget Country entity, CountryRequest request);

}
