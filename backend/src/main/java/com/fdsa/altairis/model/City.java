package com.fdsa.altairis.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "cities")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "name", nullable = false, length = 160)
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
