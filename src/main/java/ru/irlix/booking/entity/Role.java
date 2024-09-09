package ru.irlix.booking.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * Роль для пользователей
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"id", "name"})
@Entity
@Table(name = "role")
public class Role {

    /**
     * Идентификатор роли
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Schema(title = "Идентификатор роли",
            example = "14141414-1414-1414-1414-141414141414")
    @Column(name = "id")
    private UUID id;

    /**
     * Наименование роли
     */
    @Column(name = "name", nullable = false, unique = true)
    @Schema(title = "Название роли", example = "ADMIN")
    private String name;
}
