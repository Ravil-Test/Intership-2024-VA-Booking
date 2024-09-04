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
 * Пользователь
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"id", "phoneNumber", "email", "password"})
@Entity
@Table(name = "\"user\"")
public class User {

    /**
     * Идентификатор пользователя
     */
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    /**
     * ФИО пользователя
     */
    @Column(name = "fio", nullable = false)
    @Schema(title = "ФИО пользователя",
            example = "Иванов Иван Иванович")
    private String fio;

    /**
     * Номер телефона пользователя (сотовый или рабочий)
     */
    @Column(name = "phone_number", nullable = false, unique = true)
    @Schema(title = "Номер телефона пользователя",
            example = "88002000600")
    private String phoneNumber;

    /**
     * Почта пользователя
     */
    @Column(name = "email", nullable = false, unique = true)
    @Schema(title = "Электронная почта пользователя",
            example = "ivanov.dev@gmail.com")
    private String email;

    /**
     * Пароль от учётной записи пользователя
     */
    @Column(name = "password", nullable = false)
    @Schema(title = "Пароль от учётной записи пользователя",
            example = "password123")
    private char[] password;

    /**
     * Минуты, доступные пользователю для бронирования
     */
    @Column(name = "available_minutes_for_booking", nullable = false)
    @Schema(title = "Доступные минуты для бронирования",
            example = "120")
    private Integer availableMinutesForBooking;

    /**
     * Статус пользователя (удален/не удален)
     */
    @Column(name = "is_delete", nullable = false)
    @Schema(title = "Статус пользователя (удален/не удален)",
            example = "false")
    private boolean isDelete;
}