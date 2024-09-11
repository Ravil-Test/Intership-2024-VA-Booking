package ru.irlix.booking.dto.office;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Фильтр для офисов
 */
public record OfficeSearchRequest(

        @JsonProperty(value = "address")
        @Schema(title = "Адрес офиса", example = "123 Main St, Springfield")
        @Size(message = "Может быть длины от 1 до 255 символов", min = 1, max = 255)
        String address,

        @JsonProperty(value = "name")
        @Schema(title = "Название офиса", example = "Head Office")
        @Size(message = "Может быть длины от 1 до 50 символов", min = 1, max = 50)
        String name,

        @Schema(title = "Статус офиса (удален/не удален)", example = "false")
        @JsonProperty(value = "isDelete")
        Boolean isDelete) {
}
