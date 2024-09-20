package ru.irlix.booking.exceptionhandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * Обработчик исключений
 */
@ControllerAdvice(annotations = RestController.class)
public class RestControllerExceptionHandler {

    /**
     * Обрабатывает EntityNotFoundException
     *
     * @param e - проброшенное исключение
     * @return - статус 404 и сообщение об ошибке
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Обрабатывает EntityBedRequestException
     *
     * @param e - проброшенное исключение
     * @return - статус 404 и сообщение об ошибке
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleBadRequestException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
