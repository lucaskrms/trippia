package com.trippia.trippia.controller;

import com.trippia.trippia.model.Itinerary;
import com.trippia.trippia.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/itinerary")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping("/city")
    public ResponseEntity<List<Itinerary>> getItinerariesByCity(Long cityId) {
        List<Itinerary> itineraries = itineraryService.findItinerariesByCity(cityId);
        return ResponseEntity.ok(itineraries);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Itinerary>> getItinerariesByUser(Long userId) {
        List<Itinerary> itineraries = itineraryService.findItinerariesByUser(userId);
        return ResponseEntity.ok(itineraries);
    }
}
