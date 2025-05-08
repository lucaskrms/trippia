package com.trippia.trippia.controller;

import com.trippia.trippia.dto.Itinerary.CreateItineraryRequest;
import com.trippia.trippia.dto.Itinerary.UpdateItineraryRequest;
import com.trippia.trippia.model.Itinerary;
import com.trippia.trippia.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public Itinerary createItinerary(@RequestBody CreateItineraryRequest request) {
        return itineraryService.createItinerary(request);
    }

    @PostMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public Itinerary updateItinerary(@RequestBody UpdateItineraryRequest request) {
        return itineraryService.updateItinerary(request);
    }


}
