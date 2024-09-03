package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestCreate;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestUpdate;
import ru.irlix.booking.dto.breakagerequest.BreakageResponse;
import ru.irlix.booking.entity.BreakageRequest;
import ru.irlix.booking.mapper.BreakageRequestMapper;
import ru.irlix.booking.repository.BreakageRequestRepository;
import ru.irlix.booking.service.BreakageRequestService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BreakageRequestServiceImpl implements BreakageRequestService {

    private final BreakageRequestRepository breakageRequestRepository;
    private final BreakageRequestMapper breakageRequestMapper;

    @Override
    public List<BreakageResponse> getAll() {
        return breakageRequestMapper.entityToResponse(breakageRequestRepository.findAll());
    }

    @Override
    public BreakageResponse getById(UUID id) {
        BreakageRequest breakageRequest = getBreakageRequestWithNullCheck(id);
        log.info("Get breakage request by id: {} : {}", id, breakageRequest);
        return breakageRequestMapper.entityToResponse(getBreakageRequestWithNullCheck(id));
    }

    @Override
    public BreakageResponse save(@NonNull BreakageRequestCreate createBreakageRequest) {
        BreakageRequest createdBreakageRequest = breakageRequestMapper.createRequestToEntity(createBreakageRequest);

        BreakageRequest savedBreakageRequest = breakageRequestRepository.save(createdBreakageRequest);
        log.info("Created breakage request {}", savedBreakageRequest);

        return breakageRequestMapper.entityToResponse(savedBreakageRequest);
    }

    @Override
    public BreakageResponse update(UUID id, @NonNull BreakageRequestUpdate updateBreakageRequest) {
        BreakageRequest currentBreakageRequest = getBreakageRequestWithNullCheck(id);
        BreakageRequest updatedBreakageRequest = breakageRequestMapper.updateRequestToEntity(updateBreakageRequest);

        Optional.ofNullable(updatedBreakageRequest.getDescription()).ifPresent(currentBreakageRequest::setDescription);

        BreakageRequest changedBreakageRequest = breakageRequestRepository.save(currentBreakageRequest);
        log.info("Updated breakage request {}", changedBreakageRequest);

        return breakageRequestMapper.entityToResponse(changedBreakageRequest);
    }

    @Override
    public void delete(UUID id) {
        BreakageRequest breakageRequest = getBreakageRequestWithNullCheck(id);
        breakageRequestRepository.delete(breakageRequest);
        log.info("Deleted breakage request {}", breakageRequest);
    }

    /**
     * Получить зяавку с проверкой на null
     *
     * @param id - id заявки
     * @return - найденная заявка
     */
    private BreakageRequest getBreakageRequestWithNullCheck(UUID id) {
        return breakageRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Breakage request with id " + id + " not found"));
    }
}
