package com.fdsa.altairis.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class HotelResponse {

    private Long id;
    private String code;
    private String name;

    private Long cityId;
    private String cityName;

    private Long countryId;
    private String countryIso2;
    private String countryName;

    private String address;
    private Integer stars;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private String status;

}
