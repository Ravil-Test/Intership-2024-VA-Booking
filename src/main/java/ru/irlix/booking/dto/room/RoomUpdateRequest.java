package ru.irlix.booking.dto.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Запрос на создание помещения
 * DTO for {@link ru.irlix.booking.entity.Room}
 */
public record RoomUpdateRequest(

        @JsonProperty(value = "name")
        @Schema(title = "Название помещения", example = "Малый переговорный зал")
        @Size(min = 2, max = 25, message = "Минимальное название для помещения - 2 символа, максимальная - 25")
        String name,

        @JsonProperty(value = "floorNumber")
        @Schema(title = "Номер этажа", example = "3")
        @Max(value = 32_767, message = "Должно быть не более 32_767")
        @Min(value = -32_767, message = "Должно быть не менее -32_767")
        Short floorNumber,

        @JsonProperty(value = "roomNumber")
        @Schema(title = "Номер помещения", example = "15")
        @Max(value = 32_767, message = "Должно быть не более 32_767")
        @Min(value = -32_767, message = "Должно быть не менее -32_767")
        Short roomNumber,

        @JsonProperty(value = "officeId")
        @Schema(title = "Идентификатор офиса", example = "11111111-1111-1111-1111-111111111111")
        UUID officeId
) {
}