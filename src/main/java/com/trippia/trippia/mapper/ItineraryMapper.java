package com.trippia.trippia.mapper;

import com.trippia.trippia.dto.Itinerary.ItineraryResponse;
import com.trippia.trippia.model.Itinerary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CityMapper.class, UserMapper.class})
public interface ItineraryMapper {
    ItineraryResponse toResponse(Itinerary itinerary);

    List<ItineraryResponse> toResponseList(List<Itinerary> itineraries);
}
