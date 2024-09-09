package ru.irlix.booking.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


/**
 * Запрос на обновление пользователя
 * DTO for {@link ru.irlix.booking.entity.User}
 */
public record UserUpdateRequest(

        @JsonProperty(value = "fio")
        @Schema(title = "ФИО пользователя", example = "Иванов Иван Иванович")
        @Size(min = 3, max = 50, message = "Минимальная длина инициалов - 3 символа, максимальная - 50")
        String fio,

        @JsonProperty(value = "phoneNumber")
        @Schema(title = "Номер телефона пользователя",
                example = "88002000600")
        @Pattern(regexp = "^\\+?[0-9\\-\\s]*$")
        @Size(min = 5, max = 12, message = "Минимальная длина телефона - 5 символа, максимальная - 12")
        String phoneNumber,

        @JsonProperty(value = "email")
        @Email(message = "Неверный формат email")
        @Schema(title = "Электронная почта пользователя",
                example = "ivanov.dev@gmail.com")
        String email,

        @JsonProperty(value = "password")
        @Schema(title = "Пароль от учётной записи пользователя",
                example = "password123")
        char[] password
) {
}