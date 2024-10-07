package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestCreate;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestUpdate;
import ru.irlix.booking.dto.breakagerequest.BreakageResponse;
import ru.irlix.booking.dto.breakagerequest.BreakageSearchRequest;
import ru.irlix.booking.entity.BreakageRequest;
import ru.irlix.booking.entity.User;
import ru.irlix.booking.mapper.BreakageRequestMapper;
import ru.irlix.booking.repository.BreakageRequestRepository;
import ru.irlix.booking.service.BreakageRequestService;
import ru.irlix.booking.service.EmailSenderService;
import ru.irlix.booking.service.UserService;
import ru.irlix.booking.service.WorkplaceService;
import ru.irlix.booking.specification.BreakageSpecification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BreakageRequestServiceImpl implements BreakageRequestService {

    private final BreakageRequestRepository breakageRequestRepository;
    private final BreakageRequestMapper breakageRequestMapper;
    private final EmailSenderService emailSenderService;
    private final UserService userService;
    private final WorkplaceService workplaceService;

    @Override
    @Transactional(readOnly = true)
    public List<BreakageResponse> getAll() {
        return breakageRequestMapper.entityToResponse(breakageRequestRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public BreakageResponse getById(UUID id) {
        BreakageRequest breakageRequest = getBreakageRequestById(id);
        log.info("Get breakage request by id: {} : {}", id, breakageRequest);
        return breakageRequestMapper.entityToResponse(getBreakageRequestById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BreakageResponse> search(Pageable pageable, BreakageSearchRequest breakageRequest) {
        Specification<BreakageRequest> specification = Specification.where(null);

        if (breakageRequest != null && breakageRequest.requestDateTime() != null) {
            specification = specification.and(BreakageSpecification.hasRequestDateTime(breakageRequest.requestDateTime()));
        }

        if (breakageRequest != null && breakageRequest.description() != null && !breakageRequest.description().isEmpty()) {
            specification = specification.and(BreakageSpecification.hasDescription(breakageRequest.description()));
        }

        if (breakageRequest != null && breakageRequest.workplaceId() != null) {
            specification = specification.and(BreakageSpecification.hasWorkplace(breakageRequest.workplaceId()));
        }

        if (breakageRequest != null && breakageRequest.userId() != null) {
            specification = specification.and(BreakageSpecification.hasUser(breakageRequest.userId()));
        }

        if (breakageRequest != null && breakageRequest.isComplete() != null) {
            specification = specification.and(BreakageSpecification.isComplete(breakageRequest.isComplete()));
        }

        if (breakageRequest != null && breakageRequest.isCanceled() != null) {
            specification = specification.and(BreakageSpecification.isCanceled(breakageRequest.isCanceled()));
        }

        Page<BreakageRequest> breakagePage = breakageRequestRepository.findAll(specification, pageable);

        return breakagePage.map(breakageRequestMapper::entityToResponse);
    }

    @Override
    public BreakageResponse save(@NonNull BreakageRequestCreate createBreakageRequest) {
        BreakageRequest createdBreakageRequest = breakageRequestMapper.createRequestToEntity(createBreakageRequest);
        createdBreakageRequest.setUser(userService.getUserById(createBreakageRequest.user()));
        createdBreakageRequest.setWorkplace(workplaceService.getWorkplaceById(createBreakageRequest.workplace()));

        BreakageRequest savedBreakageRequest = breakageRequestRepository.save(createdBreakageRequest);
        log.info("Created breakage request {}", savedBreakageRequest);

        User user = userService.getUserById(createBreakageRequest.user());
        sendEmailAfterBooking(user.getEmail());

        return breakageRequestMapper.entityToResponse(savedBreakageRequest);
    }

    @Override
    public BreakageResponse update(UUID id, @NonNull BreakageRequestUpdate updateBreakageRequest) {
        BreakageRequest currentBreakageRequest = getBreakageRequestById(id);
        BreakageRequest updatedBreakageRequest = breakageRequestMapper.updateRequestToEntity(updateBreakageRequest);

        Optional.ofNullable(updatedBreakageRequest.getDescription()).ifPresent(currentBreakageRequest::setDescription);

        BreakageRequest changedBreakageRequest = breakageRequestRepository.save(currentBreakageRequest);
        log.info("Updated breakage request {}", changedBreakageRequest);

        return breakageRequestMapper.entityToResponse(changedBreakageRequest);
    }

    @Override
    public void delete(UUID id) {
        BreakageRequest breakageRequest = getBreakageRequestById(id);
        breakageRequestRepository.delete(breakageRequest);
        log.info("Deleted breakage request {}", breakageRequest);
    }

    /**
     * Получить заявку с проверкой на null
     *
     * @param id - id заявки
     * @return - найденная заявка
     */
    private BreakageRequest getBreakageRequestById(UUID id) {
        return breakageRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Breakage request with id " + id + " not found"));
    }

    public void sendEmailAfterBooking(String email) {
        String subject = "Breakage request created";
        String body = "Your Breakage request has been created for " + LocalDateTime.now().withNano(0);
        emailSenderService.sendEmail(email, subject, body);
    }
}
