package ru.irlix.booking.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import ru.irlix.booking.dto.room.RoomCreateRequest;
import ru.irlix.booking.dto.room.RoomResponse;
import ru.irlix.booking.dto.room.RoomUpdateRequest;

import java.util.List;
import java.util.UUID;

/**
 * Сервисный слой помещения
 */
public interface RoomService {

    /**
     * Получить помещение по id
     *
     * @param id - id помещения
     * @return - найденное помещение
     */
    RoomResponse getById(@NotNull UUID id);

    /**
     * Получить список помещений
     *
     * @return - список помещений
     */
    List<RoomResponse> getAll();

    /**
     * Создать помещение
     *
     * @return - созданное помещение
     */
    RoomResponse save(@NonNull RoomCreateRequest createRequest);

    /**
     * Обновить помещение
     *
     * @param id   - id помещения
     * @param updateRequest - обновление помещения
     * @return - обновленное помещение
     */
    RoomResponse update(@NotNull UUID id, @NonNull RoomUpdateRequest updateRequest);

    /**
     * Удалить помещение (изменения статуса isDeleted)
     *
     * @param id - id помещения
     */
    void delete(@NotNull UUID id);
}
