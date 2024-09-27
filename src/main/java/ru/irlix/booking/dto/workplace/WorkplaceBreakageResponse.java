package ru.irlix.booking.dto.workplace;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Запрос на получение рабочего места, где произошла поломка оборудования
 * DTO for {@link ru.irlix.booking.entity.Workplace}
 */
public record WorkplaceBreakageResponse(

        @JsonProperty(value = "number")
        @Schema(title = "Номер рабочего места", example = "3")
        Integer number) {
}
