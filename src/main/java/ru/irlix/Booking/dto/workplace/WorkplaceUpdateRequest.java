package ru.irlix.booking.dto.workplace;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Запрос на обновление рабочего места
 * DTO for {@link ru.irlix.booking.entity.Workplace}
 */
public record WorkplaceUpdateRequest(

        @JsonProperty(value = "number")
        @Schema(title = "Номер рабочего места", example = "3")
        @Min(message = "Минимальное значение 1", value = 1)
        @Max(message = "Максимальное значение 15", value = 15)
        @Positive(message = "Может быть строго положительным")
        Integer number,

        @JsonProperty(value = "description")
        @Schema(title = "Описание рабочего места", example = "workplace description")
        @Size(message = "Может быть не больше 255 символов", max = 255)
        String description) {
}
