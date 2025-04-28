package com.trippia.trippia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "itinerary_rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Itinerary itinerary;

    @Column(nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer rating;
}
