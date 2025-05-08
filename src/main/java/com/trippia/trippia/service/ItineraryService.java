package com.trippia.trippia.service;

import com.trippia.trippia.dto.Itinerary.CreateItineraryRequest;
import com.trippia.trippia.dto.Itinerary.Step.CreateStepRequest;
import com.trippia.trippia.dto.Itinerary.Step.UpdateStepRequest;
import com.trippia.trippia.dto.Itinerary.UpdateItineraryRequest;
import com.trippia.trippia.model.City;
import com.trippia.trippia.model.ItineraryStep;
import com.trippia.trippia.model.User;
import com.trippia.trippia.repository.CityRepository;
import com.trippia.trippia.repository.ItineraryStepRepository;
import com.trippia.trippia.repository.UserRepository;
import com.trippia.trippia.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trippia.trippia.model.Itinerary;
import com.trippia.trippia.repository.ItineraryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItineraryService {

    @Autowired
    private ItineraryRepository itineraryRepository;


    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ItineraryStepRepository itineraryStepRepository;

    @Autowired
    private ItineraryStepService itineraryStepService;

    public List<Itinerary> findItinerariesByCity(Long cityId) {
        return itineraryRepository.findByCityId(cityId);
    }

    public List<Itinerary> findItinerariesByUser(Long userId) {
        return itineraryRepository.findByUserId(userId);
    }

    public Itinerary createItinerary(CreateItineraryRequest request) {
        User currentUser = securityUtils.getCurrentUser();

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        Itinerary itinerary = new Itinerary();
        itinerary.setTitle(request.getTitle());
        itinerary.setDescription(request.getDescription());
        itinerary.setDaysQuantity(request.getDaysQuantity());
        itinerary.setCreatedAt(LocalDateTime.now());
        itinerary.setCity(city);
        itinerary.setUser(currentUser);

        itinerary = itineraryRepository.save(itinerary);

        if (request.getSteps() != null && !request.getSteps().isEmpty()) {
            Itinerary finalItinerary = itinerary;
            List<ItineraryStep> steps = request.getSteps().stream()
                    .map(stepRequest -> itineraryStepService.createStep(stepRequest, finalItinerary))
                    .collect(Collectors.toList());

            itinerary.setSteps(steps);
        }

        return itinerary;
    }

    @Transactional
    public Itinerary updateItinerary(UpdateItineraryRequest request) {
        User currentUser = securityUtils.getCurrentUser();

        Itinerary itinerary = itineraryRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        if (!itinerary.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't have permission to edit this itinerary");
        }

        Optional.ofNullable(request.getTitle()).ifPresent(itinerary::setTitle);
        Optional.ofNullable(request.getDescription()).ifPresent(itinerary::setDescription);
        Optional.ofNullable(request.getDaysQuantity()).ifPresent(itinerary::setDaysQuantity);

        itinerary = itineraryRepository.save(itinerary);

        if (request.getSteps() != null && !request.getSteps().isEmpty()) {
            for (UpdateStepRequest stepRequest : request.getSteps()) {
                itineraryStepService.updateStep(stepRequest);
            }
        }

        if (request.getNewSteps() != null && !request.getNewSteps().isEmpty()) {
            for (CreateStepRequest stepRequest : request.getNewSteps()) {
                itineraryStepService.createStep(stepRequest, itinerary);
            }
        }

        return itinerary;
    }
}