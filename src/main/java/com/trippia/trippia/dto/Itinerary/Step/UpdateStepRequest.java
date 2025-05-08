package com.trippia.trippia.dto.Itinerary.Step;

import lombok.Data;


@Data
public class UpdateStepRequest {
    private Long id;
    private Long itineraryId;
    private Integer stepOrder;
    private String placeName;
    private String type;
    private String shortDescription;
    private String distance;
    private String time;
}
