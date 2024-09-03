package ru.irlix.booking.dto.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Запрос на создание помещения
 * DTO for {@link ru.irlix.booking.entity.Room}
 */
public record RoomResponse(

        @JsonProperty(value = "name")
        @Schema(title = "Название помещения", example = "Малый переговорный зал")
        String name,

        @JsonProperty(value = "floorNumber")
        @Schema(title = "Номер этажа", example = "3")
        Short floorNumber,

        @JsonProperty(value = "roomNumber")
        @Schema(title = "Номер помещения", example = "15")
        Short roomNumber,

        @JsonProperty(value = "isDelete")
        @Schema(title = "Статус помещения (удален/не удален)", example = "false")
        boolean isDelete
) {
}
