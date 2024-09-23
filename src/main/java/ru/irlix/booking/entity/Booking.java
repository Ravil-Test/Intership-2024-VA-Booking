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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Бронирование
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"id", "cancelReason", "workplace", "user"})
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(generator = "UUID")
    @Schema(title = "Идентификатор бронирования",
            example = "00000000-0000-0000-1111-111111111111")
    @Column(name = "id")
    private UUID id;

    @Column(name = "booking_date_time", nullable = false)
    @Schema(title = "Время создания бронирования",
            example = "2024-08-30 12:11:50.077721")
    private LocalDateTime bookingDateTime;

    @Column(name = "booking_start_date_time", nullable = false)
    @Schema(title = "Время начала бронирования",
            example = "2024-08-30 12:11:50.077721")
    private LocalDateTime bookingStartDateTime;

    @Column(name = "booking_end_date_time", nullable = false)
    @Schema(title = "Время окончания бронирования",
            example = "2024-08-30 12:11:50.077721")
    private LocalDateTime bookingEndDateTime;

    @Column(name = "booking_cancel_date_time")
    @Schema(title = "Время отмены бронирования",
            example = "2024-08-30 12:11:50.077721")
    private LocalDateTime bookingCancelDateTime;

    @Column(name = "booking_cancel_reason")
    @Schema(title = "Причина отмены бронирования",
            example = "Booking canceled by Administrator. Reason: *")
    private String cancelReason;

    @Column(name = "is_booked", nullable = false)
    @Schema(title = "Статус бронирования", example = "false")
    private boolean isBooked;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "workplace_id", referencedColumnName = "id")
    private Workplace workplace;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;
}
