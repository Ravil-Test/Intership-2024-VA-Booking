package ru.irlix.booking.exceptionhandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.irlix.booking.exception.RegistrationFailedException;
import ru.irlix.booking.exception.ValidationErrorResponse;
import ru.irlix.booking.exception.Violation;

import java.util.List;

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
     * Обрабатывает IllegalArgumentException
     *
     * @param e - проброшенное исключение
     * @return - статус 400 и сообщение об ошибке
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * Обрабатывает ConstraintViolationException
     *
     * @param e - проброшенное исключение
     * @return - статус 400 и сообщение об ошибке
     */
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(violation -> new Violation(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .toList();
        return new ValidationErrorResponse(violations);
    }

    /**
     * Обрабатывает MethodArgumentNotValidException
     *
     * @param e - проброшенное исключение
     * @return - статус 400 и сообщение об ошибке
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();
        return new ValidationErrorResponse(violations);
    }

    /**
     * Обрабатывает RegistrationFailedException
     *
     * @param e - проброшенное исключение
     * @return - статус 400 и сообщение об ошибке
     */
    @ExceptionHandler(RegistrationFailedException.class)
    public ResponseEntity<String> onRegistrationFailedException(RegistrationFailedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
