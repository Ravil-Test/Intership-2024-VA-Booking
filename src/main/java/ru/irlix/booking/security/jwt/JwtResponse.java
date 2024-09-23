package ru.irlix.booking.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс для возвращения jwt-токена (controllers -> AuthController -> ("/auth")
 * Ответ пользователю при авторизации
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtResponse {

    /**
     * JWT-токен
     */
    private String token;
}
