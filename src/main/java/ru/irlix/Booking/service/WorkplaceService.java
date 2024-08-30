package ru.irlix.booking.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import ru.irlix.booking.dto.workplace.WorkplaceCreateRequest;
import ru.irlix.booking.dto.workplace.WorkplaceResponse;
import ru.irlix.booking.dto.workplace.WorkplaceUpdateRequest;

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
}
