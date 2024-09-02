package ru.irlix.booking.dto.breakagerequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Запрос на получение заявки о поломке
 */
public record BreakageResponse(

        @Schema(title = "Время создания заявки", example = "2024-08-30 12:11:50.077721")
        @JsonProperty(value = "requestDate")
        LocalDateTime requestDate,

        @Schema(title = "Описание заявки", example = "Hello, I need help, my monitor doesn't works!")
        @JsonProperty(value = "description")
        String description,

        @Schema(title = "Статус выполнения заявки", example = "false")
        @JsonProperty(value = "isComplete")
        boolean isComplete,

        @Schema(title = "Статус отмены заявки", example = "false")
        @JsonProperty(value = "isCanceled")
        boolean isCanceled) {
}