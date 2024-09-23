package ru.irlix.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.irlix.booking.dto.user.AuthenticationUserRequest;
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.dto.user.UserResponse;
import ru.irlix.booking.security.jwt.JwtResponse;
import ru.irlix.booking.service.AuthenticationService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Контроллер аутентификации")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Зарегистрировать пользователя",
            description = "Возвращает статус 201 и зарегистрированного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    public UserResponse create(@RequestBody @Valid UserCreateRequest createRequest) {
        return authenticationService.create(createRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизоваться",
            description = "Возвращает статус 200 и JWT-токен")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")

    })
    public JwtResponse login(@RequestBody @Valid AuthenticationUserRequest authenticationUserRequest) {
        return authenticationService.login(authenticationUserRequest);
    }
}
