package ru.irlix.booking.dto.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Запрос на получение бронирования
 */
public record BookingResponse(

        @JsonProperty(value = "bookingDateTime")
        @Schema(title = "Время создания бронирования", example = "2024-08-30 12:11:50.077721")
        LocalDateTime bookingDateTime,

        @JsonProperty(value = "bookingStartDateTime")
        @Schema(title = "Время начала бронирования", example = "2024-08-30 12:11:50.077721")
        LocalDateTime bookingStartDateTime,

        @JsonProperty(value = "bookingEndDateTime")
        @Schema(title = "Время окончания бронирования", example = "2024-08-30 12:11:50.077721")
        LocalDateTime bookingEndDateTime,

        @JsonProperty(value = "bookingCancelDateTime")
        @Schema(title = "Время отмены бронирования", example = "2024-08-30 12:11:50.077721")
        LocalDateTime bookingCancelDateTime,

        @JsonProperty(value = "cancelReason")
        @Schema(title = "Причина отмены", example = "Бронирования было отменено по просьбе работника")
        @Size(min = 2, max = 255, message = "Минимальный текст причины отмены - 2 символа, максимальная - 255")
        String cancelReason,

        @JsonProperty(value = "isBooked")
        @Schema(title = "Статус бронирования", example = "false")
        boolean isBooked) {
}
