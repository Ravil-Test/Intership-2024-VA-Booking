package ru.irlix.booking.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import ru.irlix.booking.dto.workplace.WorkplaceCreateRequest;
import ru.irlix.booking.dto.workplace.WorkplaceResponse;
import ru.irlix.booking.dto.workplace.WorkplaceSearchRequest;
import ru.irlix.booking.dto.workplace.WorkplaceUpdateRequest;
import ru.irlix.booking.entity.Workplace;

import java.util.List;
import java.util.UUID;

/**
 * Сервисный слой рабочих мест
 */
public interface WorkplaceService {

    /**
     * Получить рабочее место по id
     *
     * @param id - id рабочего места
     * @return - найденное рабочее место
     */
    WorkplaceResponse getById(@NotNull UUID id);

    /**
     * Получить список рабочих мест
     *
     * @return - список рабочих мест
     */
    List<WorkplaceResponse> getAll();

    /**
     * Создать рабочее место
     *
     * @param createRequest - новое рабочее место
     * @return - созданное рабочее место
     */
    WorkplaceResponse save(@NonNull WorkplaceCreateRequest createRequest);

    /**
     * Обновить рабочее место
     *
     * @param id            - id рабочего места
     * @param updateRequest - обновление рабочего места
     * @return - обновленное рабочее место
     */
    WorkplaceResponse update(@NotNull UUID id, @NonNull WorkplaceUpdateRequest updateRequest);

    /**
     * Удалить рабочее место
     *
     * @param id - id рабочего места
     */
    void delete(@NotNull UUID id);

    /**
     * Получить страницу с рабочими местами
     *
     * @param searchRequest - фильтр записей
     * @param pageable      - пагинация
     * @return - страница с рабочими местами
     */
    Page<WorkplaceResponse> search(WorkplaceSearchRequest searchRequest, Pageable pageable);

    /**
     * Получить рабочее место по id с проверкой на null
     *
     * @param id - id рабочего места
     * @return - найденное рабочее место
     */
    Workplace optionalCheck(UUID id);
}
