package ru.irlix.booking.dto.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Запрос на создание роли
 * DTO for {@link ru.irlix.booking.entity.Role}
 */
public record RoleCreateRequest(

        @JsonProperty(value = "name")
        @NotBlank(message = "Не может быть пустым")
        @Schema(title = "Название роли", example = "ADMIN")
        @Size(message = "Может быть длины от 1 до 50 символов", min = 1, max = 50)
        String name
) {
}