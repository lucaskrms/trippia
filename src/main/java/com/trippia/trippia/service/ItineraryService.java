package com.trippia.trippia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trippia.trippia.model.Itinerary;
import com.trippia.trippia.repository.ItineraryRepository;
import java.util.List;

@Service
public class ItineraryService {

    @Autowired
    private ItineraryRepository itineraryRepository;

    public List<Itinerary> findItinerariesByCity(Long cityId) {
        return itineraryRepository.findByCityId(cityId);
    }
}