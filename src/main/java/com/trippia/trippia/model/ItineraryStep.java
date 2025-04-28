package com.trippia.trippia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "itinerary_steps")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Itinerary itinerary;

    @Column(nullable = false)
    private Integer stepOrder;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String shortDescription;

    @Column(nullable = false)
    private String distance;

    @Column(nullable = false)
    private LocalDateTime time;


}
