package com.trippia.trippia.repository;

import com.trippia.trippia.model.Itinerary;

import java.util.List;

public interface ItineraryRepository {
    List<Itinerary> findByCityId(Long cityId);
}
