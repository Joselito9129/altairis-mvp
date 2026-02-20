package com.fdsa.altairis.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomTypeRequest {

    @NotNull
    private Long hotelId;

    @NotBlank @Size(max = 32)
    private String code;

    @NotBlank @Size(max = 160)
    private String name;

    @NotNull @Min(1)
    private Integer capacityAdults;

    @NotNull @Min(0)
    private Integer capacityChildren;

    @NotBlank @Size(max = 1)
    private String status;
}
