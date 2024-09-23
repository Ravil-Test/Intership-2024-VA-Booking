package ru.irlix.booking.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Запрос на получение токена доступа
 */
public record AuthenticationUserRequest(

        @Schema(title = "Логин", example = "test.dev@gmail.com")
        @JsonProperty(value = "login")
        @NotBlank(message = "Не может быть пустым")
        @Size(min = 5, max = 30, message = "Минимальная длина телефона - 5 символа, максимальная - 30")
        String login,

        @JsonProperty(value = "password")
        @NotEmpty(message = "Пароль не должен быть пустым")
        @Schema(title = "Пароль от учётной записи пользователя",
                example = "password123")
        char[] password) {
}
