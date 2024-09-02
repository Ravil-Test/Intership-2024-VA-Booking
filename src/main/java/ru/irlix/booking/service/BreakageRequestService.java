package ru.irlix.booking.service;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestCreate;
import ru.irlix.booking.dto.breakagerequest.BreakageResponse;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestUpdate;

import java.util.List;
import java.util.UUID;

/**
 * Сервисный слой заявок о поломках
 */
public interface BreakageRequestService {

    /**
     * Получить список всех заявок о поломках
     *
     * @return - список заявок
     */
    List<BreakageResponse> getAll();

    /**
     * Получить заявку по id
     *
     * @param id - id заявки
     * @return - найденая по id заявка
     */
    BreakageResponse getById(@NotNull UUID id);

    /**
     * Создание заявки о поломке
     *
     * @param createBreakageRequest - создаваемая заявка
     * @return - созданную заявку
     */
    BreakageResponse save(@NonNull BreakageRequestCreate createBreakageRequest);

    /**
     * Обновление заявки о поломке
     *
     * @param id                    - id заявки
     * @param updateBreakageRequest - обновление заявки
     * @return - обновленную заявку
     */
    BreakageResponse update(@NotNull UUID id, @NonNull BreakageRequestUpdate updateBreakageRequest);

    /**
     * Удаление заявки о поломке
     *
     * @param id - id заявки
     */
    void delete(@NotNull UUID id);
}