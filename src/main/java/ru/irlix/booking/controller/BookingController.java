package ru.irlix.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.irlix.booking.dto.booking.BookingCreateRequest;
import ru.irlix.booking.dto.booking.BookingResponse;
import ru.irlix.booking.dto.booking.BookingCancelRequest;
import ru.irlix.booking.service.BookingService;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
@Tag(name = "Контроллер бронирования")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    @Operation(summary = "Получить список бронирования",
            description = "Возвращает статус 200 и список бронирования")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public List<BookingResponse> getAll() {
        return bookingService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить бронирование по id",
            description = "Возвращает статус 200 и найденное бронирование")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public BookingResponse getById(@PathVariable UUID id) {
        return bookingService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать бронирование",
            description = "Возвращает статус 201 и созданное бронирование")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Бронирование создано"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public BookingResponse create(@RequestBody @Valid BookingCreateRequest createRequest) {
        return bookingService.save(createRequest);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить бронирование",
            description = "Возвращает статус 200 и обновленное бронирование")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Бронирование обновлено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public BookingResponse cancel(@PathVariable UUID id,
                                  @RequestBody @Valid BookingCancelRequest updateRequest) {
        return bookingService.cancel(id, updateRequest);
    }

    @PatchMapping("/confirmation/{id}")
    @Operation(summary = "Подтвердить бронирование",
            description = "Возвращает статус 200")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Бронирование подтверждено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public void confirm(@PathVariable UUID id) {
        bookingService.confirm(id);
    }
}
