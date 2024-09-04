package ru.irlix.booking.dto.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Запрос на обновление роли
 * DTO for {@link ru.irlix.booking.entity.Role}
 */
public record RoleUpdateRequest(

        @JsonProperty(value = "name")
        @Schema(title = "Название роли", example = "ADMIN")
        @Size(message = "Может быть длины от 1 до 50 символов", min = 1, max = 50)
        String name
) {
}
