package ru.irlix.booking.dto.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Запрос на создание бронирования
 */
public record BookingCreateRequest(

        @JsonProperty(value = "bookingStartDateTime")
        @Schema(title = "Время начала бронирования", example = "2024-08-30 12:11:50.077721")
        @NotNull(message = "Не может быть null")
        LocalDateTime bookingStartDateTime,

        @JsonProperty(value = "bookingEndDateTime")
        @Schema(title = "Время окончания бронирования", example = "2024-08-30 12:11:50.077721")
        @NotNull(message = "Не может быть null")
        LocalDateTime bookingEndDateTime,

        @JsonProperty(value = "userId")
        @NotNull(message = "Не может быть null")
        @Schema(title = "Идентификатор пользователя", example = "11111111-1111-1111-1111-111111111111")
        UUID userId,

        @JsonProperty(value = "workplaceId")
        @NotNull(message = "Не может быть null")
        @Schema(title = "Идентификатор рабочего места", example = "11111111-1111-1111-1111-111111111111")
        UUID workplaceId) {
}
