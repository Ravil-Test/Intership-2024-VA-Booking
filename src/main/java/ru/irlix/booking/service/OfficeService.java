package ru.irlix.booking.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import ru.irlix.booking.dto.office.OfficeCreateRequest;
import ru.irlix.booking.dto.office.OfficeResponse;
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
     * Получить офисы с пагинацией и сортировкой
     *
     * @param name     - название офиса
     * @param isDelete - статус офиса (удален/не удален
     * @param pageable - настройки пагинации
     * @return - страница с офисами
     */
    Page<OfficeResponse> getAllWithPagingAndSoring(String name, Boolean isDelete, Pageable pageable);

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

    Office getOfficeWithNullCheck(UUID id);
}
