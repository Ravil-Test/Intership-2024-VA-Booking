package ru.irlix.booking.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import ru.irlix.booking.dto.office.OfficeCreateRequest;
import ru.irlix.booking.dto.office.OfficeResponse;
import ru.irlix.booking.dto.office.OfficeSearchRequest;
import ru.irlix.booking.dto.office.OfficeUpdateRequest;
import ru.irlix.booking.entity.Office;

import java.util.List;
import java.util.UUID;

/**
 * Сервисный слой офисов
 */
public interface OfficeService {

    /**
     * Получить офис по id
     *
     * @param id - id офиса
     * @return - найденный офис
     */
    OfficeResponse getById(@NotNull UUID id);

    /**
     * Получить список офисов
     *
     * @return - список офисов
     */
    List<OfficeResponse> getAll();

    /**
     * Создать офис
     *
     * @param createRequest - создаваемый офис
     * @return - созданный офис
     */
    OfficeResponse save(@NonNull OfficeCreateRequest createRequest);

    /**
     * Обновить офис
     *
     * @param id            - id офиса
     * @param updateRequest - обновление офиса
     * @return - обновленный офис
     */
    OfficeResponse update(@NotNull UUID id, @NonNull OfficeUpdateRequest updateRequest);

    /**
     * Удалить офис
     *
     * @param id - id офиса
     */
    void delete(@NotNull UUID id);

    /**
     * Получить офис на id с проверкой на null
     *
     * @param id - id офиса
     * @return - найденный офис
     */
    Office getOfficeById(UUID id);

    /**
     * Получить страницу со списком офисов по фильтру
     *
     * @param searchRequest - фильтр
     * @param pageable      - пагинация
     * @return - страница со списков рабочих мест
     */
    Page<OfficeResponse> search(OfficeSearchRequest searchRequest, Pageable pageable);
}
