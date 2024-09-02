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

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Заявка о поломке
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "breakage_request")
public class BreakageRequest {

    @Id
    @GeneratedValue
    @Schema(title = "Идентификатор заявки о поломке", example = "11111111-1111-1111-1111-111111111111")
    @Column(name = "id")
    private UUID id;

    @Schema(title = "Время создания заявки о поломке", example = "2024-08-30 12:11:50.077721")
    @Column(name = "request_datetime", nullable = false)
    private LocalDateTime requestDate;

    @Schema(title = "Описание заявки о поломке", example = "Hello, I need help, my monitor doesn't works!")
    @Column(name = "description", nullable = false)
    private String description;

    @Schema(title = "Статус выполнения заявки о поломке", example = "false")
    @Column(name = "is_completed")
    private boolean isComplete;

    @Schema(title = "Статус отмены заявки о поломке", example = "false")
    @Column(name = "is_canceled")
    private boolean isCanceled;
}