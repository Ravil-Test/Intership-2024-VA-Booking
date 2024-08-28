package ru.irlix.booking.dto.office;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Запрос на получение офиса
 * DTO for {@link ru.irlix.booking.entity.Office}
 */
public record OfficeResponse(

        @Schema(title = "Адрес офиса", example = "123 Main St, Springfield")
        @JsonProperty(value = "address")
        String address,

        @Schema(title = "Название офиса", example = "Head Office")
        @JsonProperty(value = "name")
        String name,

        @Schema(title = "Статус офиса (удален/не удален)", example = "false")
        @JsonProperty(value = "isDelete")
        boolean isDelete) {
}
