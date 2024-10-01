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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@ToString(exclude = {"id", "description", "workplace", "user"})
@Table(name = "breakage_request")
@Builder
public class BreakageRequest {

    @Id
    @GeneratedValue
    @Schema(title = "Идентификатор заявки о поломке", example = "11111111-1111-1111-1111-111111111111")
    @Column(name = "id")
    private UUID id;

    @Schema(title = "Время создания заявки о поломке", example = "2024-08-30 12:11:50.077721")
    @Column(name = "request_date_time", nullable = false)
    private LocalDateTime requestDateTime;

    @Schema(title = "Описание заявки о поломке", example = "Hello, I need help, my monitor doesn't works!")
    @Column(name = "description", nullable = false)
    private String description;

    @Schema(title = "Статус выполнения заявки о поломке", example = "false")
    @Column(name = "is_completed")
    private boolean isComplete;

    @Schema(title = "Статус отмены заявки о поломке", example = "false")
    @Column(name = "is_canceled")
    private boolean isCanceled;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "workplace_id", referencedColumnName = "id")
    private Workplace workplace;

    /**
     * Информация о пользователе
     */
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;
}