package ru.irlix.booking.dto.office;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Запрос на создание офиса
 * DTO for {@link ru.irlix.booking.entity.Office}
 */
public record OfficeCreateRequest(

        @JsonProperty(value = "address")
        @NotBlank(message = "Не может быть пустым")
        @Schema(title = "Адрес офиса", example = "123 Main St, Springfield")
        @Size(message = "Может быть длины от 1 до 255 символов", min = 1, max = 255)
        String address,

        @JsonProperty(value = "name")
        @NotBlank(message = "Не может быть пустым")
        @Schema(title = "Название офиса", example = "Head Office")
        @Size(message = "Может быть длины от 1 до 50 символов", min = 1, max = 50)
        String name) {
}
