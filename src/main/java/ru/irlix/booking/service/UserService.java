package ru.irlix.booking.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.dto.user.UserResponse;
import ru.irlix.booking.dto.user.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

/**
 * Сервисный слой пользователей
 */
public interface UserService {

    /**
     * Получить пользователя по id
     *
     * @param id - id пользователя
     * @return - найденный пользователь
     */
    UserResponse getById(@NotNull UUID id);

    /**
     * Получить список пользователей
     *
     * @return - список пользователей
     */
    List<UserResponse> getAll();

    /**
     * Создать пользователя
     *
     * @return - созданный пользователь
     */
    UserResponse save(@NonNull UserCreateRequest createRequest);

    /**
     * Обновить пользователя
     *
     * @param id            - id пользователя
     * @param updateRequest - обновление пользователя
     * @return - обновленный пользователь
     */
    UserResponse update(@NotNull UUID id, @NonNull UserUpdateRequest updateRequest);

    /**
     * Удалить пользователя (изменения статуса isDeleted)
     *
     * @param id - id пользователя
     */
    void delete(@NotNull UUID id);
}
