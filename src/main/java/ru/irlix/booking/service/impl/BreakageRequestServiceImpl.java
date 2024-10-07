package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestCreate;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestUpdate;
import ru.irlix.booking.dto.breakagerequest.BreakageResponse;
import ru.irlix.booking.dto.breakagerequest.BreakageSearchRequest;
import ru.irlix.booking.entity.Booking;
import ru.irlix.booking.entity.BreakageRequest;
import ru.irlix.booking.entity.User;
import ru.irlix.booking.mapper.BreakageRequestMapper;
import ru.irlix.booking.repository.BreakageRequestRepository;
import ru.irlix.booking.service.BreakageRequestService;
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

    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";
    private final BreakageRequestRepository breakageRequestRepository;
    private final BreakageRequestMapper breakageRequestMapper;
    private final WorkplaceService workplaceService;
    private final UserService userService;
    private Authentication authentication;

    @Override
    @Transactional(readOnly = true)
    public List<BreakageResponse> getAll() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        User requestOwner = getUserByLogin(authentication.getName());
        List<BreakageRequest> response = null;

        if (requestOwner.getRoles().stream().anyMatch((role -> role.getName().equals(USER_ROLE)))) {
            response = breakageRequestRepository.getBreakageRequestByUserId(requestOwner.getId());
        } else if (requestOwner.getRoles().stream().anyMatch((role -> role.getName().equals(ADMIN_ROLE)))) {
            response = breakageRequestRepository.findAll();
        }

        return breakageRequestMapper.entityToResponse(response);
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
    @Transactional
    public BreakageResponse save(@NonNull BreakageRequestCreate createBreakageRequest) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        User requestOwner = getUserByLogin(authentication.getName());

        boolean canSubmitRequest = false;

        for (Booking booking : requestOwner.getBookings()) {
            if (checkAvailabilityOfReservation(booking.getWorkplace().getId(), createBreakageRequest.workplaceId())
                    && validDateTimeCheck(booking)) {
                canSubmitRequest = true;
            }
        }

        if (canSubmitRequest) {
            BreakageRequest createdBreakageRequest = breakageRequestMapper.createRequestToEntity(createBreakageRequest);
            createdBreakageRequest.setUser(requestOwner);
            createdBreakageRequest.setWorkplace(workplaceService.getWorkplaceById(createBreakageRequest.workplaceId()));

            BreakageRequest savedBreakageRequest = breakageRequestRepository.save(createdBreakageRequest);
            log.info("Created breakage request {}", savedBreakageRequest);
            return breakageRequestMapper.entityToResponse(savedBreakageRequest);
        } else {
            throw new IllegalArgumentException("Вы не можете подать заявку о поломке на данное рабочее место.");
        }
    }

    @Override
    @Transactional
    public BreakageResponse update(UUID id, @NonNull BreakageRequestUpdate updateBreakageRequest) {
        BreakageRequest currentBreakageRequest = getBreakageRequestById(id);
        BreakageRequest updatedBreakageRequest = breakageRequestMapper.updateRequestToEntity(updateBreakageRequest);

        Optional.ofNullable(updatedBreakageRequest.getDescription()).ifPresent(currentBreakageRequest::setDescription);

        BreakageRequest changedBreakageRequest = breakageRequestRepository.save(currentBreakageRequest);
        log.info("Updated breakage request {}", changedBreakageRequest);

        return breakageRequestMapper.entityToResponse(changedBreakageRequest);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        breakageRequestRepository.changeBreakageRequestIsCanceled(id, true);
        log.info("Deleted breakage request with id: {}", id);
    }

    /**
     * Проверка может ли пользователь оформить заявку о поломке на данное рабочее место
     *
     * @param reservationId      - id рабочих мест с бронью
     * @param workplaceRequestId - id рабочего места, на которое пользователь хочет оформить заявку о поломке
     * @return - флаг может ли оформить
     */
    private boolean checkAvailabilityOfReservation(UUID reservationId, UUID workplaceRequestId) {
        log.info("Проверка возможности пользователя оформить заявку о поломке для данного рабочего места");
        if (!reservationId.equals(workplaceRequestId)) {
            throw new IllegalArgumentException("Вы не можете подать заявку о поломке на рабочее место, которое вы не забронировали");
        } else
            return true;
    }

    /**
     * Проверка валидности даты и времени бронирования при оформлении заявки
     *
     * @param booking - бронирование
     * @return - флаг, что заявка может быть оформлена
     */
    private boolean validDateTimeCheck(Booking booking) {
        log.info("Проверка, валидности времени при оформлении заявки");
        if (LocalDateTime.now().isBefore(booking.getBookingStartDateTime())) {
            throw new IllegalArgumentException("Вы не можете оформить заявку, так как ваше бронирование еще не началось");
        } else if (LocalDateTime.now().isAfter(booking.getBookingEndDateTime().plusDays(1))) {
            throw new IllegalArgumentException("Вы не можете оформить заявку, так как с момента окончания бронирования прошло более 24 часов");
        } else
            return true;
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

    /**
     * Получить пользователя по прешедшему логину
     *
     * @param login - логин
     * @return - пользователь
     */
    @NonNull
    public User getUserByLogin(String login) {
        User response;
        if (login.matches("^\\+?[0-9\\-\\s]*$")) {
            response = userService.getUserByPhoneNumber(login);
        } else if (login.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            response = userService.getUserByEmail(login);
        } else
            throw new EntityNotFoundException("User not found");
        return response;
    }
}
