package ru.irlix.booking.dto.room;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Запрос для фильтра для получения помещений
 *
 * @param name
 * @param isDelete
 * @param floorNumber
 * @param roomNumber
 * @param officeId
 */
public record RoomSearchRequest(

        @Schema(title = "Название помещения", example = "Малый переговорный зал")
        @Size(min = 3, max = 25, message = "Минимальное название для помещения - 3 символа, максимальная - 25")
        String name,

        Boolean isDelete,

        @Schema(title = "Номер этажа", example = "3")
        @Max(value = 32_767, message = "Должно быть не более 32_767")
        @Min(value = -32_767, message = "Должно быть не менее -32_767")
        Short floorNumber,

        @Schema(title = "Номер помещения", example = "15")
        @Max(value = 32_767, message = "Должно быть не более 32_767")
        @Min(value = -32_767, message = "Должно быть не менее -32_767")
        Short roomNumber,

        @Schema(title = "Идентификатор офиса", example = "11111111-1111-1111-1111-111111111111")
        UUID officeId) {
}
