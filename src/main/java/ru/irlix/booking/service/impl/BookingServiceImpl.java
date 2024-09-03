package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.irlix.booking.dto.booking.BookingCancelRequest;
import ru.irlix.booking.dto.booking.BookingCreateRequest;
import ru.irlix.booking.dto.booking.BookingResponse;
import ru.irlix.booking.entity.Booking;
import ru.irlix.booking.mapper.BookingMapper;
import ru.irlix.booking.repository.BookingRepository;
import ru.irlix.booking.service.BookingService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

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
    public void confirm(UUID id) {
        bookingRepository.changeBookingToConfirmed(id, true);
        log.info("Booking with id : {} is confirmed", id);
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
