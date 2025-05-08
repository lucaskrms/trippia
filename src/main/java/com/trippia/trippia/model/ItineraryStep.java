package com.trippia.trippia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "itinerary_steps")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_id", nullable = false)
    @JsonIgnore
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
    private String time;
}
