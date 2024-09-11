package ru.irlix.booking.dto.workplace;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * Запрос на фильтр для рабочих мест
 */
public record WorkplaceSearchRequest(

        @JsonProperty(value = "number")
        @Schema(title = "Номер рабочего места", example = "3")
        @Min(message = "Минимальное значение 1", value = 1)
        @Max(message = "Максимальное значение 50", value = 50)
        @Positive(message = "Может быть строго положительным")
        Integer number,

        @JsonProperty(value = "isDelete")
        @Schema(title = "Статус рабочего места (удален/не удален)", example = "false")
        Boolean isDelete,

        @JsonProperty(value = "roomId")
        @Schema(title = "Идентификатор помещения", example = "44444444-4444-4444-4444-444444444444")
        UUID roomId) {
}
