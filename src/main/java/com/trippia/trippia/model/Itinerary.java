package com.trippia.trippia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "itineraries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer daysQuantity;

    @Column(nullable = false)
    private Double averageRating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private City city;

    @Column(nullable = false)
    private User user;

}