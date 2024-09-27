package ru.irlix.booking.dto.breakagerequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Запрос на фильтр заявки о поломке
 * DTO for {@link ru.irlix.booking.entity.BreakageRequest}
 */
public record BreakageSearchRequest(

        @Schema(title = "Время создания заявки", example = "2024-08-30 12:11:50.077721")
        @JsonProperty(value = "requestDateTime")
        LocalDateTime requestDateTime,

        @Schema(title = "Описание заявки", example = "Hello, I need help, my monitor doesn't works!")
        @JsonProperty(value = "description")
        String description,

        @Schema(title = "Статус выполнения заявки", example = "false")
        @JsonProperty(value = "isComplete")
        Boolean isComplete,

        @Schema(title = "Статус отмены заявки", example = "false")
        @JsonProperty(value = "isCanceled")
        Boolean isCanceled,

        @Schema(title = "ID рабочего места", example = "55555555-5555-5555-5555-555555555555")
        @JsonProperty(value = "workplace_id")
        UUID workplaceId,

        @Schema(title = "ID пользователя", example = "12121212-1212-1212-1212-121212121212")
        @JsonProperty(value = "user_id")
        UUID userId) {
}