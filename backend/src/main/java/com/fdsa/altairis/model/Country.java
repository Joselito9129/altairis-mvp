package com.fdsa.altairis.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "countries")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iso2", nullable = false, length = 2, unique = true)
    private String iso2;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "status", nullable = false, length = 1)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_user", nullable = false, length = 100, updatable = false)
    private String createdUser;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_user", length = 100)
    private String updatedUser;
}
