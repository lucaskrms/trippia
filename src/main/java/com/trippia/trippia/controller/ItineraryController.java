package com.trippia.trippia.controller;

import com.trippia.trippia.dto.Itinerary.CreateItineraryRequest;
import com.trippia.trippia.dto.Itinerary.ItineraryResponse;
import com.trippia.trippia.dto.Itinerary.UpdateItineraryRequest;
import com.trippia.trippia.service.ItineraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
@Tag(name = "Itineraries", description = "Endpoints for managing itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;

    @GetMapping("/city/{cityId}")
    @Operation(summary = "Get all itineraries by city")
    public ResponseEntity<List<ItineraryResponse>> getItinerariesByCity(
            @PathVariable Long cityId) {
        return ResponseEntity.ok(itineraryService.findItinerariesByCity(cityId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all itineraries by user")
    public ResponseEntity<List<ItineraryResponse>> getItinerariesByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(itineraryService.findItinerariesByUser(userId));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new itinerary")
    public ResponseEntity<ItineraryResponse> createItinerary(
            @Valid @RequestBody CreateItineraryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itineraryService.createItinerary(request));
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update an existing itinerary")
    public ResponseEntity<ItineraryResponse> updateItinerary(
            @Valid @RequestBody UpdateItineraryRequest request) {
        return ResponseEntity.ok(itineraryService.updateItinerary(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete an itinerary")
    public ResponseEntity<Void> deleteItinerary(@PathVariable Long id) {
        itineraryService.deleteItinerary(id);
        return ResponseEntity.noContent().build();
    }
}
