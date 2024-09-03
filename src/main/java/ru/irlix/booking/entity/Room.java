package ru.irlix.booking.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Помещение
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
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
    @Schema(title = "Номер этажа", example = "3")
    private Short floorNumber;

    /**
     * Номер помещения
     */
    @Column(name = "room_number", nullable = false)
    @Schema(title = "Номер помещения", example = "15")
    private Short roomNumber;

    /**
     * Статус помещения (удален/не удален)
     */
    @Column(name = "is_delete", nullable = false)
    @Schema(title = "Статус помещения (удален/не удален)",
            example = "false")
    private boolean isDelete;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "office_id", referencedColumnName = "id")
    private Office office;

    /**
     * Рабочие места офиса
     */
    @OneToMany(mappedBy = "room", cascade = {CascadeType.REMOVE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private List<Workplace> workplaces;
}