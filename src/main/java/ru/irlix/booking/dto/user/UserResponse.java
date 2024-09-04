package ru.irlix.booking.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Запрос на создание ользователя
 * DTO for {@link ru.irlix.booking.entity.User}
 */
public record UserResponse(

        @JsonProperty(value = "fio")
        @Schema(title = "ФИО пользователя", example = "Иванов Иван Иванович")
        String fio,

        @JsonProperty(value = "phoneNumber")
        @Schema(title = "Номер телефона пользователя",
                example = "88002000600")
        String phoneNumber,

        @JsonProperty(value = "email")
        @Schema(title = "Электронная почта пользователя",
                example = "ivanov.dev@gmail.com")
        String email,

        @JsonProperty(value = "availableMinutesForBooking")
        @Schema(title = "Доступные минуты для бронирования",
                example = "120")
        Integer availableMinutesForBooking,

        @JsonProperty(value = "isDelete")
        @Schema(title = "Статус пользователя (удален/не удален)",
                example = "false")
        boolean isDelete
) {
}