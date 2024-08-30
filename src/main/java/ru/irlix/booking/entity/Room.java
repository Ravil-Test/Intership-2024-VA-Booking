package ru.irlix.booking.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "room")
public class Room {

    /**
     * Идентификатор помещения
     */
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    /**
     * Название помещения
     */
    @Column(name = "name", nullable = false)
    @Schema(title = "Название помещения",
            example = "Малый переговорный зал")
    private String name;

    /**
     * Номер этажа, на котором располагается помещение
     */
    @Column(name = "floor_number", nullable = false)
    @Schema(title = "Номер этажа",example = "3")
    private Short floorNumber;

    /**
     * Номер помещения
     */
    @Column(name = "room_number", nullable = false)
    @Schema(title = "Номер помещения",example = "15")
    private Short roomNumber;

    /**
     * Статус помещения (удален/не удален)
     */
    @Column(name = "is_delete", nullable = false)
    @Schema(title = "Статус помещения (удален/не удален)",
            example = "false")
    private boolean isDelete;
}