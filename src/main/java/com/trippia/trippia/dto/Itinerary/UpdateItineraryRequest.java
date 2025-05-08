package com.trippia.trippia.dto.Itinerary;

import com.trippia.trippia.dto.Itinerary.Step.CreateStepRequest;
import com.trippia.trippia.dto.Itinerary.Step.UpdateStepRequest;
import lombok.Data;

import java.util.List;

@Data
public class UpdateItineraryRequest {
    private Long id;
    private String title;
    private String description;
    private Integer daysQuantity;
    private Long cityId;
    private List<UpdateStepRequest> steps;
    private List<CreateStepRequest> newSteps;
}
