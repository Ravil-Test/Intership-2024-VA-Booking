package ru.irlix.booking.service;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestCreate;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestUpdate;
import ru.irlix.booking.dto.breakagerequest.BreakageResponse;
import ru.irlix.booking.dto.breakagerequest.BreakageSearchRequest;

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
     * @return - найденная по id заявка
     */
    BreakageResponse getById(@NotNull UUID id);

    /**
     * Фильтр для поиска заявок о поломке
     *
     * @param pageable - пагинация
     * @param breakageRequest - ДТО заявки для поиска
     * @return - список пользователей
     */
    Page<BreakageResponse> search(Pageable pageable, BreakageSearchRequest breakageRequest);

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