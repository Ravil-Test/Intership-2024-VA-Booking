package ru.irlix.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.irlix.booking.dto.office.OfficeCreateRequest;
import ru.irlix.booking.dto.office.OfficeResponse;
import ru.irlix.booking.dto.office.OfficeUpdateRequest;
import ru.irlix.booking.service.api.OfficeService;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/office")
@Tag(name = "Контроллер офисов")
public class OfficeController {

    private final OfficeService officeService;

    @GetMapping
    @Operation(summary = "Получить список офисов",
            description = "Возвращает статус 200 и список офисов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public List<OfficeResponse> getAll() {
        return officeService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить офис по id",
            description = "Возвращает статус 200 и найденный офис")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "404", description = "Офис не найден"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public OfficeResponse getById(@PathVariable UUID id) {
        return officeService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать офис",
            description = "Возвращает статус 201 и созданный офис")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Офис создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public OfficeResponse createOffice(@RequestBody @Valid OfficeCreateRequest createRequest) {
        return officeService.save(createRequest);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить офис",
            description = "Возвращает статус 200 и обновленный офис")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Офис обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Офис не найден"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public OfficeResponse updateOffice(@PathVariable UUID id, @RequestBody @Valid OfficeUpdateRequest updateRequest) {
        return officeService.update(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить офис",
            description = "Возвращает статус 200")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Офис удален"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Офис не найден"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public void deleteOffice(@PathVariable UUID id) {
        officeService.delete(id);
    }
}
