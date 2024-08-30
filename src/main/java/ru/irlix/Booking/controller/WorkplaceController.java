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
import ru.irlix.booking.dto.workplace.WorkplaceCreateRequest;
import ru.irlix.booking.dto.workplace.WorkplaceResponse;
import ru.irlix.booking.dto.workplace.WorkplaceUpdateRequest;
import ru.irlix.booking.service.WorkplaceService;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/workplace")
@Tag(name = "Контроллер рабочих мест")
public class WorkplaceController {

    private final WorkplaceService workplaceService;

    @GetMapping
    @Operation(summary = "Получить список рабочих мест",
            description = "Возвращает статус 200 и список рабочих мест")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public List<WorkplaceResponse> getAll() {
        return workplaceService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить рабочее место по id",
            description = "Возвращает статус 200 и найденное рабочее место")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "404", description = "Рабочее место не найдено"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public WorkplaceResponse getWorkplaceById(@PathVariable UUID id) {
        return workplaceService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать рабочее место",
            description = "Возвращает статус 201 и созданное рабочее место")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Рабочее место создано"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public WorkplaceResponse createWorkplace(@RequestBody @Valid WorkplaceCreateRequest createRequest) {
        return workplaceService.save(createRequest);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновить рабочее место",
            description = "Возвращает статус 200 и обновленное рабочее место")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Рабочее место обновлено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Рабочее место не найдено"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public WorkplaceResponse updateWorkplace(@PathVariable UUID id, @RequestBody @Valid WorkplaceUpdateRequest updateRequest) {
        return workplaceService.update(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить рабочее место",
            description = "Возвращает статус 200")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Рабочее место удалено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Рабочее место не найдено"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public void deleteWorkplace(@PathVariable UUID id) {
        workplaceService.delete(id);
    }
}
