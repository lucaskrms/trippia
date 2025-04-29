package com.trippia.trippia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trippia.trippia.model.City;
import com.trippia.trippia.repository.CityRepository;
import java.util.Optional;

@Service
public class CityService {
    
    @Autowired
    private CityRepository cityRepository;

    public Optional<City> findByCityName(String cityName) {
        return cityRepository.findByName(cityName);
    }

    public City addCity(City city) {
        return cityRepository.save(city);
    }
}