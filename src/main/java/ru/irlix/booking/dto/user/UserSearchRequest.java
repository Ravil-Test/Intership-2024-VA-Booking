package ru.irlix.booking.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;


/**
 * Запрос на фильтр для пользователей
 * DTO for {@link ru.irlix.booking.entity.User}
 */
public record UserSearchRequest(

        @JsonProperty(value = "fio")
        @Schema(title = "ФИО пользователя", example = "Иванов Иван Иванович")
        @Size(min = 3, max = 50, message = "Минимальная длина инициалов - 3 символа, максимальная - 50")
        String fio,

        @JsonProperty(value = "isDelete")
        @Schema(title = "Статус пользователя (удален/не удален)",
                example = "false")
        Boolean isDelete
) {
}