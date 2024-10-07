package ru.irlix.booking.dto.breakagerequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Запрос на создание заявки о поломке
 */
public record BreakageRequestCreate(

        @Schema(title = "Описание заявки")
        @JsonProperty(value = "description")
        @NotBlank(message = "Не может быть пустым")
        @Size(message = "Может быть длины от 1 до 255 символов", min = 1, max = 255)
        String description,

        @Schema(title = "Информация о рабочем месте", example = "99999999-9999-9999-9999-999999999999")
        @JsonProperty(value = "workplaceId")
        @NotNull(message = "Не может быть null")
        UUID workplaceId
) {
}
