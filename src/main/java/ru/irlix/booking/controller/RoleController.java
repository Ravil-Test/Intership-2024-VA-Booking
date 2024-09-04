package ru.irlix.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.irlix.booking.dto.role.RoleCreateRequest;
import ru.irlix.booking.dto.role.RoleResponse;
import ru.irlix.booking.dto.role.RoleUpdateRequest;
import ru.irlix.booking.service.RoleService;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Роль для пользователей", description = "Данный контроллер содержит CRUD операции с сущностью роль")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @Operation(summary = "Получить список ролей",
            description = "Возвращает статус 200 и список ролей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public List<RoleResponse> getAll() {
        return roleService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить роль по id",
            description = "Возвращает статус 200 и найденную роль")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "404", description = "Помещение не найдено"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public RoleResponse getById(@PathVariable UUID id) {
        return roleService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать роль",
            description = "Возвращает статус 200 и созданную роль")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public RoleResponse create(@RequestBody @Valid RoleCreateRequest createRequest) {
        return roleService.save(createRequest);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление роли",
            description = "Возвращает статус 200 и обновленную роль")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль обновлена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Роль не найдена"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public RoleResponse update(@PathVariable UUID id,
                               @RequestBody @Valid RoleUpdateRequest updateRequest) {
        return roleService.update(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить роль",
            description = "Возвращает статус 200")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль удалена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Роль не найдена"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public void delete(@PathVariable UUID id) {
        roleService.delete(id);
    }
}
