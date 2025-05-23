package com.trippia.trippia.dto.Itinerary;

import com.trippia.trippia.dto.Itinerary.Step.CreateStepRequest;
import lombok.Data;

import java.util.List;

@Data
public class CreateItineraryRequest {
    private String title;
    private String description;
    private Integer daysQuantity;
    private Long cityId;
    private List<CreateStepRequest> steps;
}