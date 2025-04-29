package com.trippia.trippia.dto.Itinerary;

import lombok.Data;

@Data
public class CreateItineraryRequest {
    private String title;
    private String description;
    private Integer daysQuantity;
    private Long cityId;
}