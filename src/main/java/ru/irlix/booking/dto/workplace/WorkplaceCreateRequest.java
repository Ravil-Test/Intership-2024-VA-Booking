package ru.irlix.booking.dto.workplace;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Запрос на создание рабочего места
 * DTO for {@link ru.irlix.booking.entity.Workplace}
 */
public record WorkplaceCreateRequest(

        @JsonProperty(value = "number")
        @NotNull(message = "Не может быть null")
        @Schema(title = "Номер рабочего места", example = "3")
        @Min(message = "Минимальное значение 1", value = 1)
        @Max(message = "Максимальное значение 15", value = 15)
        @Positive(message = "Может быть строго положительным")
        Integer number,

        @JsonProperty(value = "description")
        @NotBlank(message = "Не может быть пустым")
        @Size(message = "Может быть не больше 255 символов", max = 255)
        @Schema(title = "Описание рабочего места", example = "workplace description")
        String description,

        @JsonProperty(value = "roomId")
        @NotNull(message = "Не может быть null")
        @Schema(title = "Идентификатор помещения", example = "44444444-4444-4444-4444-444444444444")
        UUID roomId
) {
}
