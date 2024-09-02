package ru.irlix.booking.dto.breakagerequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Запрос на обновление заявки о поломке
 */
public record BreakageRequestUpdate(

        @Schema(title = "Описание заявки")
        @JsonProperty(value = "description")
        @NotBlank(message = "Не может быть пустым")
        @Size(message = "Может быть длины от 1 до 255 символов", min = 1, max = 255)
        String description) {
}