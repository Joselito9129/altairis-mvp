package com.fdsa.altairis.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CityRequest {

    @NotNull
    private Long countryId;

    @NotBlank
    @Size(max = 160)
    private String name;

    @NotBlank
    @Size(max = 1)
    private String status;
}
