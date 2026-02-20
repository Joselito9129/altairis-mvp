package com.fdsa.altairis.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "inventory_days")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "total_units", nullable = false)
    private Integer totalUnits;

    @Column(name = "available_units", nullable = false)
    private Integer availableUnits;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

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
