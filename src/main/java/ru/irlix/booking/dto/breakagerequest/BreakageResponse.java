package ru.irlix.booking.dto.breakagerequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import ru.irlix.booking.dto.user.UserBreakageResponse;
import ru.irlix.booking.dto.workplace.WorkplaceBreakageResponse;

import java.time.LocalDateTime;

/**
 * Запрос на получение заявки о поломке
 * DTO for {@link ru.irlix.booking.entity.BreakageRequest}
 */
public record BreakageResponse(

        @Schema(title = "Время создания заявки", example = "2024-08-30 12:11:50.077721")
        @JsonProperty(value = "requestDateTime")
        LocalDateTime requestDateTime,

        @Schema(title = "Описание заявки", example = "Hello, I need help, my monitor doesn't works!")
        @JsonProperty(value = "description")
        String description,

        @Schema(title = "Статус выполнения заявки", example = "false")
        @JsonProperty(value = "isComplete")
        boolean isComplete,

        @Schema(title = "Статус отмены заявки", example = "false")
        @JsonProperty(value = "isCanceled")
        boolean isCanceled,

        @Schema(title = "Информация о рабочем месте", example = "рабочее место, где произошла поломка оборудования")
        @JsonProperty(value = "workplace")
        WorkplaceBreakageResponse workplace,

        @Schema(title = "Информация о пользователе", example = "пользователь, у которого произошла поломка оборудования")
        @JsonProperty(value = "user")
        UserBreakageResponse user) {
}