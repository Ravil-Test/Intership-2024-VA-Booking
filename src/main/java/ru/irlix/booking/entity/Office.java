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
 * Офис
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "office")
public class Office {

    /**
     * Идентификатор офиса
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Schema(title = "Идентификатор офиса",
            example = "11111111-1111-1111-1111-111111111111")
    @Column(name = "id")
    private UUID id;

    /**
     * Адрес офиса
     */
    @Column(name = "address", nullable = false)
    @Schema(title = "Адрес офиса",
            example = "789 Oak St, Capital City")
    private String address;

    /**
     * Название офиса
     */
    @Column(name = "name", nullable = false)
    @Schema(title = "Название офиса",
            example = "Remote Office")
    private String name;

    /**
     * Статус офиса (удален/не удален)
     */
    @Column(name = "is_delete", nullable = false)
    @Schema(title = "Статус офиса (удален/не удален)",
            example = "false")
    private boolean isDelete;
}
