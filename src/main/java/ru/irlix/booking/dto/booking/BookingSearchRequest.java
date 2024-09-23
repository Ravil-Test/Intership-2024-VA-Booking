package ru.irlix.booking.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record BookingSearchRequest(

        @Schema(title = "Идентификатор пользователя", example = "12121212-1212-1212-1212-121212121212")
        UUID userId,

        @Schema(title = "Идентификатор рабочего места", example = "77777777-7777-7777-7777-777777777777")
        UUID workplaceId,

        @Schema(title = "Статус бронирования", example = "false")
        Boolean isBooked) {
}
