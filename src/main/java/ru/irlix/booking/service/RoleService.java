package ru.irlix.booking.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import ru.irlix.booking.dto.role.RoleCreateRequest;
import ru.irlix.booking.dto.role.RoleResponse;
import ru.irlix.booking.dto.role.RoleUpdateRequest;

import java.util.List;
import java.util.UUID;

/**
 * Сервисный слой роли
 */
public interface RoleService {

    /**
     * Получить роль по id
     *
     * @param id - id роли
     * @return - найденная роль
     */
    RoleResponse getById(@NotNull UUID id);

    /**
     * Получить список ролей
     *
     * @return - список ролей
     */
    List<RoleResponse> getAll();

    /**
     * Создать роль
     *
     * @return - созданная роль
     */
    RoleResponse save(@NonNull RoleCreateRequest createRequest);

    /**
     * Обновить роль
     *
     * @param id            - id роли
     * @param updateRequest - обновление роли
     * @return - обновленная роль
     */
    RoleResponse update(@NotNull UUID id, @NonNull RoleUpdateRequest updateRequest);

    /**
     * Удалить роль
     *
     * @param id - id роли
     */
    void delete(@NotNull UUID id);
}
