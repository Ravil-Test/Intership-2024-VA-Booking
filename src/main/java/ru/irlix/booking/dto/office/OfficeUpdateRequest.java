package ru.irlix.booking.dto.office;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Запрос на обновления офиса
 * DTO for {@link ru.irlix.booking.entity.Office}
 */
public record OfficeUpdateRequest(

        @Schema(title = "Адрес офиса", example = "123 Main St, Springfield")
        @JsonProperty(value = "address")
        @Size(message = "Может быть длины от 1 до 255 символов", min = 1, max = 255)
        String address,

        @Schema(title = "Название офиса", example = "Head Office")
        @JsonProperty(value = "name")
        @Size(message = "Может быть длины от 1 до 50 символов", min = 1, max = 50)
        String name) {
}
