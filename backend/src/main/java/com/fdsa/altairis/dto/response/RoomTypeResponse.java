package com.fdsa.altairis.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomTypeResponse {
    private Long id;
    private Long hotelId;
    private String hotelCode;
    private String hotelName;
    private String code;
    private String name;
    private Integer capacityAdults;
    private Integer capacityChildren;
    private String status;

}
