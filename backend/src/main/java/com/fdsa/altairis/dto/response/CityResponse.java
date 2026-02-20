package com.fdsa.altairis.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CityResponse {

    private Long id;
    private Long countryId;
    private String countryIso2;
    private String countryName;
    private String name;
    private String status;

}
