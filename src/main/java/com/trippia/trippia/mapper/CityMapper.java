package com.trippia.trippia.mapper;

import com.trippia.trippia.dto.City.CityResponse;
import com.trippia.trippia.model.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityResponse toResponse(City city);
}
