package com.trippia.trippia.service;

import com.trippia.trippia.dto.Itinerary.Step.CreateStepRequest;
import com.trippia.trippia.dto.Itinerary.Step.UpdateStepRequest;
import com.trippia.trippia.exception.InvalidRequestException;
import com.trippia.trippia.exception.ResourceNotFoundException;
import com.trippia.trippia.model.Itinerary;
import com.trippia.trippia.model.ItineraryStep;
import com.trippia.trippia.repository.ItineraryRepository;
import com.trippia.trippia.repository.ItineraryStepRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItineraryStepService {

    private final ItineraryStepRepository stepRepository;
    private final ItineraryRepository itineraryRepository;

    /**
     * Cria um novo passo para um itinerário
     *
     * @param stepRequest Request contendo os dados do passo
     * @param itinerary Itinerário ao qual o passo pertence
     * @return O passo criado
     * @throws InvalidRequestException se a request for inválida
     */
    @Transactional
    public ItineraryStep createStep(CreateStepRequest stepRequest, Itinerary itinerary) {
        log.debug("Creating new step for itinerary ID: {}", itinerary.getId());

        try {
            validateCreateStepRequest(stepRequest);
            validateItinerary(itinerary);
            validateStepOrder(stepRequest.getStepOrder(), itinerary);

            ItineraryStep step = buildStep(stepRequest, itinerary);
            ItineraryStep savedStep = stepRepository.save(step);

            log.info("Step created successfully. Step ID: {}, Itinerary ID: {}",
                    savedStep.getId(), itinerary.getId());

            return savedStep;

        } catch (Exception e) {
            log.error("Error creating step for itinerary {}: {}",
                    itinerary.getId(), e.getMessage(), e);
            throw new ServiceException("Failed to create step", e);
        }
    }

    /**
     * Atualiza um passo existente
     *
     * @param request Request contendo os dados atualizados
     * @throws ResourceNotFoundException se o passo não for encontrado
     * @throws InvalidRequestException se a request for inválida
     */
    @Transactional
    public void updateStep(UpdateStepRequest request) {
        log.debug("Updating step ID: {}", request.getId());

        try {
            validateUpdateStepRequest(request);

            ItineraryStep step = findStepById(request.getId());
            validateStepBelongsToItinerary(step, request.getItineraryId());

            updateStepFields(step, request);
            stepRepository.save(step);

            log.info("Step updated successfully. Step ID: {}", step.getId());

        } catch (Exception e) {
            log.error("Error updating step {}: {}", request.getId(), e.getMessage(), e);
            throw new ServiceException("Failed to update step", e);
        }
    }

    /**
     * Deleta um passo
     *
     * @param stepId ID do passo a ser deletado
     * @throws ResourceNotFoundException se o passo não for encontrado
     */
    @Transactional
    public void deleteStep(Long stepId) {
        log.debug("Deleting step ID: {}", stepId);

        try {
            ItineraryStep step = findStepById(stepId);
            stepRepository.delete(step);

            log.info("Step deleted successfully. Step ID: {}", stepId);

        } catch (Exception e) {
            log.error("Error deleting step {}: {}", stepId, e.getMessage(), e);
            throw new ServiceException("Failed to delete step", e);
        }
    }


    private void validateCreateStepRequest(CreateStepRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Step request cannot be null");
        }

        if (request.getStepOrder() == null || request.getStepOrder() < 1) {
            throw new InvalidRequestException("Step order must be greater than zero");
        }

        if (!StringUtils.hasText(request.getPlaceName())) {
            throw new InvalidRequestException("Place name is required");
        }

        if (!StringUtils.hasText(request.getType())) {
            throw new InvalidRequestException("Step type is required");
        }

        if (!StringUtils.hasText(request.getShortDescription())) {
            throw new InvalidRequestException("Short description is required");
        }
    }

    private void validateUpdateStepRequest(UpdateStepRequest request) {
        if (request == null || request.getId() == null) {
            throw new InvalidRequestException("Invalid update request");
        }

        if (request.getStepOrder() != null && request.getStepOrder() < 1) {
            throw new InvalidRequestException("Step order must be greater than zero");
        }
    }

    private void validateItinerary(Itinerary itinerary) {
        if (itinerary == null || itinerary.getId() == null) {
            throw new InvalidRequestException("Invalid itinerary");
        }

        if (!itineraryRepository.existsById(itinerary.getId())) {
            throw new ResourceNotFoundException("Itinerary not found: " + itinerary.getId());
        }
    }

    private void validateStepOrder(Integer stepOrder, Itinerary itinerary) {
        boolean orderExists = stepRepository.existsByStepOrderAndItineraryId(
                stepOrder, itinerary.getId());

        if (orderExists) {
            throw new InvalidRequestException(
                    String.format("Step order %d already exists in itinerary %d",
                            stepOrder, itinerary.getId()));
        }
    }

    private void validateStepBelongsToItinerary(ItineraryStep step, Long itineraryId) {
        if (!step.getItinerary().getId().equals(itineraryId)) {
            throw new InvalidRequestException("Step does not belong to the specified itinerary");
        }
    }

    private ItineraryStep findStepById(Long stepId) {
        return stepRepository.findById(stepId)
                .orElseThrow(() -> new ResourceNotFoundException("Step not found: " + stepId));
    }

    private ItineraryStep buildStep(CreateStepRequest request, Itinerary itinerary) {
        return ItineraryStep.builder()
                .stepOrder(request.getStepOrder())
                .placeName(request.getPlaceName())
                .type(request.getType())
                .shortDescription(request.getShortDescription())
                .distance(request.getDistance())
                .time(request.getTime())
                .itinerary(itinerary)
                .build();
    }

    private void updateStepFields(ItineraryStep step, UpdateStepRequest request) {
        if (request.getStepOrder() != null) {
            step.setStepOrder(request.getStepOrder());
        }
        if (StringUtils.hasText(request.getPlaceName())) {
            step.setPlaceName(request.getPlaceName());
        }
        if (StringUtils.hasText(request.getType())) {
            step.setType(request.getType());
        }
        if (StringUtils.hasText(request.getShortDescription())) {
            step.setShortDescription(request.getShortDescription());
        }
        if (StringUtils.hasText(request.getDistance())) {
            step.setDistance(request.getDistance());
        }
        if (StringUtils.hasText(request.getTime())) {
            step.setTime(request.getTime());
        }
    }
}
