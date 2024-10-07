package ru.irlix.booking.dto.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Запрос на отмену бронирования
 */
public record BookingCancelRequest(

        @JsonProperty(value = "cancelReason")
        @Schema(title = "Причина отмены", example = "Бронирования было отменено по просьбе работника")
        @Size(min = 2, max = 255, message = "Минимальный текст причины отмены - 2 символа, максимальная - 255")
        String cancelReason) {
}
