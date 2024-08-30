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

import java.util.UUID;

/**
 * Рабочее место
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workplace")
public class Workplace {

    /**
     * Идентификатор рабочего места
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    @Schema(title = "Идентификатор рабочего места",
            example = "33333333-3333-3333-3333-333333333333")
    private UUID id;

    /**
     * Номер рабочего места
     */
    @Column(name = "number", nullable = false)
    @Schema(title = "Номер рабочего места",
            example = "2")
    private Integer number;

    /**
     * Описание
     */
    @Column(name = "description", nullable = false)
    @Schema(title = "Описание",
            example = "workplace description")
    private String description;

    /**
     * Статус (удален/не удален)
     */
    @Column(name = "is_delete", nullable = false)
    @Schema(title = "Статус (удален/не удален)",
            example = "false")
    private boolean isDelete;
}
