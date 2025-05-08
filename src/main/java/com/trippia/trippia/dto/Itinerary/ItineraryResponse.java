package com.trippia.trippia.dto.Itinerary;

import com.trippia.trippia.dto.City.CityResponse;
import com.trippia.trippia.dto.Itinerary.Step.StepResponse;
import com.trippia.trippia.dto.User.UserResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItineraryResponse {
    private Long id;
    private String title;
    private String description;
    private Integer daysQuantity;
    private LocalDateTime createdAt;
    private CityResponse city;
    private UserResponse user;
    private List<StepResponse> steps;

}
