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
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.dto.user.UserResponse;
import ru.irlix.booking.dto.user.UserUpdateRequest;
import ru.irlix.booking.service.UserService;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователь", description = "Данный контроллер содержит CRUD операции с сущностью пользователь")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получить список пользователей",
            description = "Возвращает статус 200 и список пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public List<UserResponse> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по id",
            description = "Возвращает статус 200 и найденного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public UserResponse getById(@PathVariable UUID id) {
        return userService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать пользователя",
            description = "Возвращает статус 200 и созданного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public UserResponse create(@RequestBody @Valid UserCreateRequest createRequest) {
        return userService.save(createRequest);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить пользователя",
            description = "Возвращает статус 200 и обновлённого пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Помещение не найдено"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public UserResponse update(@PathVariable UUID id,
                               @RequestBody @Valid UserUpdateRequest updateRequest) {
        return userService.update(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя",
            description = "Возвращает статус 200")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь удалён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }
}
