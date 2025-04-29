package com.trippia.trippia.repository;

import com.trippia.trippia.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    List<Itinerary> findByCityId(Long cityId);

    List<Itinerary> findByUserId(Long userId);
}