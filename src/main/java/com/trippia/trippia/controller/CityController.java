package com.trippia.trippia.controller;

import com.trippia.trippia.model.City;
import com.trippia.trippia.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/popular")
    public ResponseEntity<List<City>> getCitiesByItineraryCount() {
        List<City> cities = cityService.getCitiesByItineraryCount();
        return ResponseEntity.ok(cities);
    }
}
