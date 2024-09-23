package ru.irlix.booking.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import ru.irlix.booking.dto.booking.BookingCancelRequest;
import ru.irlix.booking.dto.booking.BookingCreateRequest;
import ru.irlix.booking.dto.booking.BookingResponse;
import ru.irlix.booking.dto.booking.BookingSearchRequest;

import java.util.List;
import java.util.UUID;

public interface BookingService {

    /**
     * Получить список бронирования
     *
     * @return - список бронирования
     */
    List<BookingResponse> getAll();

    /**
     * Получить бронирование по id
     *
     * @param id - id бронирования
     * @return - найденное бронирование
     */
    BookingResponse getById(@NotNull UUID id);

    /**
     * Создать бронирование
     *
     * @param createRequest - создаваемое бронирование
     * @return - созданное бронирование
     */
    BookingResponse save(@NonNull BookingCreateRequest createRequest);

    /**
     * Отменить бронирование
     *
     * @param id            - id бронирования
     * @param updateRequest - обновление бронирования
     * @return - отмена бронирование
     */
    BookingResponse cancel(@NotNull UUID id, @NonNull BookingCancelRequest updateRequest);

    /**
     * Подтверждение бронирования
     *
     * @param id - id бронирования
     */
    void confirm(@NotNull UUID id);

    /**
     * Поиск и сортировка бронирований
     *
     * @param searchRequest - фильтр
     * @param pageable - пагинация
     * @return - страница с бронированиями
     */
    Page<BookingResponse> findAll(BookingSearchRequest searchRequest, Pageable pageable);
}
