package com.fdsa.altairis.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class HotelRequest {

    @NotBlank
    @Size(max = 32)
    private String code;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotNull
    private Long cityId;

    @Size(max = 240)
    private String address;

    @Min(1) @Max(5)
    private Integer stars;

    private BigDecimal latitude;
    private BigDecimal longitude;

    @NotBlank
    @Size(max = 1)
    private String status;

}
