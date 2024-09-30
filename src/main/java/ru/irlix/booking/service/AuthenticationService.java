package ru.irlix.booking.service;

import ru.irlix.booking.dto.user.AuthenticationUserRequest;
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.dto.user.UserResponse;
import ru.irlix.booking.security.jwt.JwtResponse;

/**
 * Бизнес-логика аутентификации
 */
public interface AuthenticationService {

    /**
     * Создать JWT-токен (авторизация)
     *
     * @param authRequest - тело с логином и паролем
     * @return - JWT-токен
     */
    JwtResponse login(AuthenticationUserRequest authRequest);

    /**
     * Создать нового пользователя
     *
     * @param createRequest - новый пользователь
     * @return - созданный пользователь
     */
    UserResponse registration(UserCreateRequest createRequest);
}
