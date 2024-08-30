package ru.irlix.booking.dto.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Запрос на создание помещения
 * DTO for {@link ru.irlix.booking.entity.Room}
 */
public record RoomCreateRequest(

        @JsonProperty(value = "name")
        @NotBlank(message = "Имя не должно быть пустым")
        @Schema(title = "Название помещения", example = "Малый переговорный зал")
        @Size(min = 3, max = 15, message = "Минимальное название для помещения - 3 символа, максимальная - 15")
        String name,

        @JsonProperty(value = "floorNumber")
        @Size(min = -32_768, max = 32_767, message = "Размер от -32 768 до 32 767")
        @Schema(title = "Номер этажа", example = "3")
        @NotNull
        Short floorNumber,

        @JsonProperty(value = "roomNumber")
        @Size(min = -32_768, max = 32_767, message = "Размер от -32 768 до 32 767")
        @Schema(title = "Номер помещения", example = "15")
        @NotNull
        Short roomNumber
) {
}
