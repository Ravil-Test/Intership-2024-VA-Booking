package ru.irlix.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.dto.user.UserResponse;
import ru.irlix.booking.dto.user.UserSearchRequest;
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
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
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
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
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

    @GetMapping("/search")
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Поиск пользователей",
            description = "Возвращает статус 200 и список пользователей с фильтрацией по fio или статусу is_deleted")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public Page<UserResponse> search(@RequestBody @Parameter(description = "Фильтр пользователей")
                                     @Valid UserSearchRequest searchRequest,
                                     @PageableDefault(sort = "fio") Pageable pageable) {
        return userService.search(pageable, searchRequest);
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать пользователя",
            description = "Возвращает статус 201 и созданного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public UserResponse create(@RequestBody @Valid UserCreateRequest createRequest) {
        return userService.save(createRequest);
    }

    @PatchMapping("/{id}")
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Обновить пользователя",
            description = "Возвращает статус 200 и обновлённого пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public UserResponse update(@PathVariable UUID id,
                               @RequestBody @Valid UserUpdateRequest updateRequest) {
        return userService.update(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
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
