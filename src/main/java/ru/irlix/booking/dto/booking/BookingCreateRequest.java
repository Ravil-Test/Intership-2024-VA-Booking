package ru.irlix.booking.dto.booking;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Запрос на создание бронирования
 */
public record BookingCreateRequest(

        @JsonProperty(value = "bookingStartDateTime")
        @Schema(title = "Время начала бронирования", example = "2024-08-30 12:11:50.077721")
        LocalDateTime bookingStartDateTime,

        @JsonProperty(value = "bookingEndDateTime")
        @Schema(title = "Время окончания бронирования", example = "2024-08-30 12:11:50.077721")
        LocalDateTime bookingEndDateTime) {
}
