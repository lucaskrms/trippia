package com.trippia.trippia.service;

import com.trippia.trippia.dto.Itinerary.Step.CreateStepRequest;
import com.trippia.trippia.dto.Itinerary.Step.UpdateStepRequest;
import com.trippia.trippia.model.Itinerary;
import com.trippia.trippia.model.ItineraryStep;
import com.trippia.trippia.repository.ItineraryRepository;
import com.trippia.trippia.repository.ItineraryStepRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItineraryStepService {

    @Autowired
    ItineraryStepRepository repository;

    @Autowired
    ItineraryRepository itineraryRepository;


    public ItineraryStep createStep(CreateStepRequest stepRequest, Itinerary itinerary) {
        ItineraryStep step = new ItineraryStep();
        step.setStepOrder(stepRequest.getStepOrder());
        step.setPlaceName(stepRequest.getPlaceName());
        step.setType(stepRequest.getType());
        step.setShortDescription(stepRequest.getShortDescription());
        step.setDistance(stepRequest.getDistance());
        step.setTime(stepRequest.getTime());
        step.setItinerary(itinerary);

        return repository.save(step);
    }

    @Transactional
    public void updateStep(UpdateStepRequest request) {
        ItineraryStep step = repository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Step not found"));

        Optional.ofNullable(request.getStepOrder()).ifPresent(step::setStepOrder);
        Optional.ofNullable(request.getPlaceName()).ifPresent(step::setPlaceName);
        Optional.ofNullable(request.getType()).ifPresent(step::setType);
        Optional.ofNullable(request.getShortDescription()).ifPresent(step::setShortDescription);
        Optional.ofNullable(request.getDistance()).ifPresent(step::setDistance);
        Optional.ofNullable(request.getTime()).ifPresent(step::setTime);

        repository.save(step);
    }

    public void deleteStep(Long stepId) {
        if (!repository.existsById(stepId)) {
            throw new EntityNotFoundException("Step not found");
        }
        repository.deleteById(stepId);
    }
}
