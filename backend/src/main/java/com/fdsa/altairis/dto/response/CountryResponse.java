package com.fdsa.altairis.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class CountryResponse {
    private Long id;
    private String iso2;
    private String name;
    private String status;

    private Instant createdAt;
    private String createdUser;
    private Instant updatedAt;
    private String updatedUser;
}
