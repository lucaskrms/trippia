package com.trippia.trippia.repository;

import com.trippia.trippia.model.Itinerary;
import com.trippia.trippia.model.ItineraryStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraryStepRepository extends JpaRepository<ItineraryStep, Long> {
    List<ItineraryStep> findByItineraryIdOrderByStepOrder(Long itineraryId);
    boolean existsByStepOrderAndItineraryId(Integer stepOrder, Long itineraryId);
}
