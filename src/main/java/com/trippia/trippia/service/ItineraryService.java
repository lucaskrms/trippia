package com.trippia.trippia.service;

import com.trippia.trippia.dto.Itinerary.CreateItineraryRequest;
import com.trippia.trippia.dto.Itinerary.ItineraryResponse;
import com.trippia.trippia.dto.Itinerary.Step.CreateStepRequest;
import com.trippia.trippia.dto.Itinerary.UpdateItineraryRequest;
import com.trippia.trippia.exception.InvalidRequestException;
import com.trippia.trippia.exception.ResourceNotFoundException;
import com.trippia.trippia.exception.UnauthorizedException;
import com.trippia.trippia.model.City;
import com.trippia.trippia.model.ItineraryStep;
import com.trippia.trippia.model.User;
import com.trippia.trippia.repository.CityRepository;
import com.trippia.trippia.mapper.ItineraryMapper;
import com.trippia.trippia.util.SecurityUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import com.trippia.trippia.model.Itinerary;
import com.trippia.trippia.repository.ItineraryRepository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItineraryService {
    private final ItineraryRepository repository;
    private final CityRepository cityRepository;
    private final SecurityUtils securityUtils;
    private final ItineraryStepService itineraryStepService;
    private final ItineraryMapper itineraryMapper;

    /**
     * Finds all itineraries for a specific city
     * @param cityId ID of the city
     * @return List of itineraries
     * @throws ResourceNotFoundException if city doesn't exist
     */
    public List<ItineraryResponse> findItinerariesByCity(Long cityId) {
        log.debug("Fetching itineraries for city: {}", cityId);

        try {
            validateCity(cityId);

            List<Itinerary> itineraries = repository.findByCityId(cityId);
            log.info("Found {} itineraries for city {}", itineraries.size(), cityId);

            return itineraries.stream()
                    .map(itineraryMapper::toResponse)
                    .collect(Collectors.toList());

        } catch (ResourceNotFoundException e) {
            log.warn("City not found: {}", cityId);
            throw e;
        } catch (Exception e) {
            log.error("Error fetching itineraries for city {}: {}", cityId, e.getMessage(), e);
            throw new ServiceException("Failed to fetch itineraries", e);
        }
    }

    /**
     * Finds all itineraries for a specific user
     * @param userId ID of the user
     * @return List of itineraries
     * @throws ResourceNotFoundException if user doesn't exist
     */
    public List<ItineraryResponse> findItinerariesByUser(Long userId) {
        log.debug("Fetching itineraries for user: {}", userId);

        try {
            List<Itinerary> itineraries = repository.findByUserId(userId);
            log.info("Found {} itineraries for user {}", itineraries.size(), userId);

            return itineraries.stream()
                    .map(itineraryMapper::toResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error fetching itineraries for user {}: {}", userId, e.getMessage(), e);
            throw new ServiceException("Failed to fetch user itineraries", e);
        }
    }

    /**
     * Creates a new itinerary
     * @param request Creation request containing itinerary details
     * @return Created itinerary
     */
    @Transactional
    public ItineraryResponse createItinerary(CreateItineraryRequest request) {
        log.debug("Creating new itinerary: {}", request.getTitle());

        try {
            validateCreateRequest(request);

            User currentUser = securityUtils.getCurrentUser();
            City city = findCity(request.getCityId());

            Itinerary itinerary = buildItinerary(request, currentUser, city);
            itinerary = repository.save(itinerary);

            processSteps(request.getSteps(), itinerary);

            log.info("Successfully created itinerary with ID: {}", itinerary.getId());
            return itineraryMapper.toResponse(itinerary);

        } catch (InvalidRequestException | ResourceNotFoundException | UnauthorizedException e) {
            log.warn("Validation failed during itinerary creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error creating itinerary: {}", e.getMessage(), e);
            throw new ServiceException("Failed to create itinerary", e);
        }
    }

    /**
     * Updates an existing itinerary
     * @param request Update request containing modified itinerary details
     * @return Updated itinerary
     */
    @Transactional
    public ItineraryResponse updateItinerary(UpdateItineraryRequest request) {
        log.debug("Updating itinerary with ID: {}", request.getId());

        try {
            validateUpdateRequest(request);

            Itinerary itinerary = findAndValidateItinerary(request.getId());
            updateItineraryFields(itinerary, request);

            processUpdatedSteps(request, itinerary);

            log.info("Successfully updated itinerary with ID: {}", itinerary.getId());
            return itineraryMapper.toResponse(itinerary);

        } catch (InvalidRequestException | ResourceNotFoundException | UnauthorizedException e) {
            log.warn("Validation failed during itinerary update: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error updating itinerary {}: {}", request.getId(), e.getMessage(), e);
            throw new ServiceException("Failed to update itinerary", e);
        }
    }

    /**
     * Deletes an itinerary
     * @param itineraryId ID of the itinerary to delete
     */
    @Transactional
    public void deleteItinerary(Long itineraryId) {
        log.debug("Deleting itinerary with ID: {}", itineraryId);

        try {
            Itinerary itinerary = findAndValidateItinerary(itineraryId);
            repository.delete(itinerary);

            log.info("Successfully deleted itinerary with ID: {}", itineraryId);

        } catch (ResourceNotFoundException | UnauthorizedException e) {
            log.warn("Validation failed during itinerary deletion: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error deleting itinerary {}: {}", itineraryId, e.getMessage(), e);
            throw new ServiceException("Failed to delete itinerary", e);
        }
    }

    private void validateCreateRequest(CreateItineraryRequest request) {
        log.debug("Validating create itinerary request");

        if (request == null) {
            throw new InvalidRequestException("Request cannot be null");
        }

        if (StringUtils.isBlank(request.getTitle())) {
            throw new InvalidRequestException("Title is required");
        }

        if (StringUtils.isBlank(request.getDescription())) {
            throw new InvalidRequestException("Description is required");
        }

        if (request.getDaysQuantity() == null || request.getDaysQuantity() < 1) {
            throw new InvalidRequestException("Days quantity must be greater than zero");
        }

        if (request.getCityId() == null) {
            throw new InvalidRequestException("City ID is required");
        }

        validateStepsRequest(request.getSteps());
    }

    private void validateUpdateRequest(UpdateItineraryRequest request) {
        log.debug("Validating update itinerary request");

        if (request == null || request.getId() == null) {
            throw new InvalidRequestException("Request and ID cannot be null");
        }

        if (request.getTitle() != null && StringUtils.isBlank(request.getTitle())) {
            throw new InvalidRequestException("Title cannot be blank");
        }

        if (request.getDaysQuantity() != null && request.getDaysQuantity() < 1) {
            throw new InvalidRequestException("Days quantity must be greater than zero");
        }

        if (!CollectionUtils.isEmpty(request.getNewSteps())) {
            validateStepsRequest(request.getNewSteps());
        }
    }

    private void validateStepsRequest(List<CreateStepRequest> steps) {
        if (CollectionUtils.isEmpty(steps)) {
            throw new InvalidRequestException("At least one step is required");
        }

        Set<Integer> orders = new HashSet<>();
        for (CreateStepRequest step : steps) {
            if (step.getStepOrder() == null || step.getStepOrder() < 1) {
                throw new InvalidRequestException("Step order must be greater than zero");
            }
            if (!orders.add(step.getStepOrder())) {
                throw new InvalidRequestException("Duplicate step order: " + step.getStepOrder());
            }
        }
    }

    private City findCity(Long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("City not found with ID: %d", cityId)));
    }

    private void validateCity(Long cityId) {
        if (!cityRepository.existsById(cityId)) {
            throw new ResourceNotFoundException(
                    String.format("City not found with ID: %d", cityId));
        }
    }

    private Itinerary findAndValidateItinerary(Long itineraryId) {
        Itinerary itinerary = repository.findById(itineraryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Itinerary not found with ID: %d", itineraryId)));

        User currentUser = securityUtils.getCurrentUser();
        if (!itinerary.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You don't have permission to access this itinerary");
        }

        return itinerary;
    }

    private Itinerary buildItinerary(CreateItineraryRequest request, User user, City city) {
        return Itinerary.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .daysQuantity(request.getDaysQuantity())
                .createdAt(LocalDateTime.now())
                .city(city)
                .user(user)
                .build();
    }

    private void processSteps(List<CreateStepRequest> stepRequests, Itinerary itinerary) {
        if (CollectionUtils.isEmpty(stepRequests)) {
            return;
        }

        List<ItineraryStep> steps = stepRequests.stream()
                .map(stepRequest -> itineraryStepService.createStep(stepRequest, itinerary))
                .collect(Collectors.toList());

        itinerary.setSteps(steps);
    }

    private void updateItineraryFields(Itinerary itinerary, UpdateItineraryRequest request) {
        Optional.ofNullable(request.getTitle())
                .filter(StringUtils::isNotBlank)
                .ifPresent(itinerary::setTitle);

        Optional.ofNullable(request.getDescription())
                .filter(StringUtils::isNotBlank)
                .ifPresent(itinerary::setDescription);

        Optional.ofNullable(request.getDaysQuantity())
                .filter(days -> days > 0)
                .ifPresent(itinerary::setDaysQuantity);
    }

    private void processUpdatedSteps(UpdateItineraryRequest request, Itinerary itinerary) {
        if (!CollectionUtils.isEmpty(request.getSteps())) {
            request.getSteps().forEach(itineraryStepService::updateStep);
        }

        if (!CollectionUtils.isEmpty(request.getNewSteps())) {
            request.getNewSteps().forEach(
                    stepRequest -> itineraryStepService.createStep(stepRequest, itinerary));
        }
    }
}