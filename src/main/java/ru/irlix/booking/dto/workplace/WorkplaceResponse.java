package ru.irlix.booking.dto.workplace;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Запрос на получение рабочего места
 * DTO for {@link ru.irlix.booking.entity.Workplace}
 */
public record WorkplaceResponse(

        @JsonProperty(value = "number")
        @Schema(title = "Номер рабочего места", example = "3")
        Integer number,

        @JsonProperty(value = "description")
        @Schema(title = "Описание рабочего места", example = "workplace description")
        String description,

        @Schema(title = "Статус рабочего места (удален/не удален)", example = "false")
        @JsonProperty(value = "isDelete")
        boolean isDelete) {
}
