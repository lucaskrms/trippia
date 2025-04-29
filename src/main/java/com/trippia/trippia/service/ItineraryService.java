package com.trippia.trippia.service;

import com.trippia.trippia.dto.Itinerary.CreateItineraryRequest;
import com.trippia.trippia.model.City;
import com.trippia.trippia.model.User;
import com.trippia.trippia.repository.CityRepository;
import com.trippia.trippia.repository.UserRepository;
import com.trippia.trippia.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.trippia.trippia.model.Itinerary;
import com.trippia.trippia.repository.ItineraryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItineraryService {

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private SecurityUtils securityUtils;

    public List<Itinerary> findItinerariesByCity(Long cityId) {
        return itineraryRepository.findByCityId(cityId);
    }

    public List<Itinerary> findItinerariesByUser(Long userId) {
        return itineraryRepository.findByUserId(userId);
    }

    public Itinerary createItinerary(CreateItineraryRequest request) {
        User user = securityUtils.getCurrentUser();

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        Itinerary itinerary = new Itinerary();
        itinerary.setTitle(request.getTitle());
        itinerary.setDescription(request.getDescription());
        itinerary.setDaysQuantity(request.getDaysQuantity());
        itinerary.setAverageRating(0.0);
        itinerary.setCreatedAt(LocalDateTime.now());
        itinerary.setCity(city);
        itinerary.setUser(user);

        return itineraryRepository.save(itinerary);
    }
}