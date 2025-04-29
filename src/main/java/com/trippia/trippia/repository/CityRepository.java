package com.trippia.trippia.repository;

import com.trippia.trippia.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByName(String name);

    List<City> findByCountry(String country);

    List<City> findByNameContainingIgnoreCase(String name);

    List<City> findAllByOrderByNameAsc();

    @Query("SELECT c FROM City c LEFT JOIN c.itineraries i GROUP BY c.id ORDER BY COUNT(i) DESC")
    List<City> findAllOrderByItineraryCountDesc();
}