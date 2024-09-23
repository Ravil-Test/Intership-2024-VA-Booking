package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.booking.dto.booking.BookingCancelRequest;
import ru.irlix.booking.dto.booking.BookingCreateRequest;
import ru.irlix.booking.dto.booking.BookingResponse;
import ru.irlix.booking.dto.booking.BookingSearchRequest;
import ru.irlix.booking.entity.Booking;
import ru.irlix.booking.mapper.BookingMapper;
import ru.irlix.booking.repository.BookingRepository;
import ru.irlix.booking.service.BookingService;
import ru.irlix.booking.service.UserService;
import ru.irlix.booking.service.WorkplaceService;
import ru.irlix.booking.specification.BookingSpecifications;
import ru.irlix.booking.specification.RoomSpecification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final WorkplaceService workplaceService;

    @Override
    public List<BookingResponse> getAll() {
        return bookingMapper.entityListToResponseList(bookingRepository.findAll());
    }

    @Override
    public BookingResponse getById(UUID id) {
        Booking foundBooking = getBookingWithNullCheck(id);
        log.info("Get booking with id: {} : {}", id, foundBooking);
        return bookingMapper.entityToResponse(foundBooking);
    }

    @Override
    public BookingResponse save(@NonNull BookingCreateRequest createRequest) {
        Booking booking = bookingMapper.createRequestToEntity(createRequest);

        booking.setUser(userService.getUserWithNullCheck(createRequest.userID()));
        booking.setWorkplace(workplaceService.optionalCheck(createRequest.workplaceID()));

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Created booking : {}", savedBooking);

        return bookingMapper.entityToResponse(savedBooking);
    }

    @Override
    public BookingResponse cancel(UUID id, @NonNull BookingCancelRequest updateRequest) {
        Booking currentBooking = getBookingWithNullCheck(id);
        Booking updateForBooking = bookingMapper.cancelRequestToEntity(updateRequest);

        Optional.ofNullable(updateForBooking.getCancelReason()).ifPresent(currentBooking::setCancelReason);

        Booking changedBooking = bookingRepository.save(currentBooking);
        log.info("Cancel booking with id: {} : {}", id, changedBooking);

        return bookingMapper.entityToResponse(changedBooking);
    }

    @Override
    @Transactional
    public void confirm(UUID id) {
        bookingRepository.changeBookingToConfirmed(id, true);
        log.info("Booking with id : {} is confirmed", id);
    }

    @Override
    public Page<BookingResponse> findAll(BookingSearchRequest searchRequest, Pageable pageable) {
        Specification<Booking> specification = Specification.where(null);

        if (searchRequest.userId() != null) {
            specification = specification.and(BookingSpecifications.hasUserId(searchRequest.userId()));
        }

        if (searchRequest.workplaceId() != null) {
            specification = specification.and(BookingSpecifications.hasWorkplaceId(searchRequest.workplaceId()));
        }

        if (searchRequest.isBooked() != null) {
            specification = specification.and(BookingSpecifications.isBooked(searchRequest.isBooked()));
        }

        Page<Booking> responsePage = bookingRepository.findAll(specification, pageable);

        return responsePage.map(bookingMapper::entityToResponse);
    }

    /**
     * Получить бронирование с проверкой на null
     *
     * @param id - id бронирование
     * @return - найденное бронирование
     */
    private Booking getBookingWithNullCheck(UUID id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id " + id + " not found"));
    }
}