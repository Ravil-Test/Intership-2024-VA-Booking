package ru.irlix.booking.dto.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Запрос на получение роли
 * DTO for {@link ru.irlix.booking.entity.Role}
 */
public record RoleResponse(

        @JsonProperty(value = "name")
        @Schema(title = "Название роли", example = "ADMIN")
        String name
) {
}