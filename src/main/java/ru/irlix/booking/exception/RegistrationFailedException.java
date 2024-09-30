package ru.irlix.booking.exception;

/**
 * Исключение ошибки при регистрации
 */
public class RegistrationFailedException extends RuntimeException {
    public RegistrationFailedException(String message) {
        super(message);
    }
}
