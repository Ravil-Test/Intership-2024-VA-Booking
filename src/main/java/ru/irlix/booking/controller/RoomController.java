package ru.irlix.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
import ru.irlix.booking.dto.room.RoomCreateRequest;
import ru.irlix.booking.dto.room.RoomResponse;
import ru.irlix.booking.dto.room.RoomSearchRequest;
import ru.irlix.booking.dto.room.RoomUpdateRequest;
import ru.irlix.booking.service.RoomService;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Tag(name = "Помещение", description = "Данный контроллер содержит CRUD операции с сущностью помещение")
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Получить список помещений",
            description = "Возвращает статус 200 и список офисов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public List<RoomResponse> getAll() {
        return roomService.getAll();
    }

    @PostMapping("/search")
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Получить страницу со списком помещений",
            description = "Возвращает статус 200 и страницу с помещениями")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "400", description = "Некорректно переданные параметры")
    })
    public Page<RoomResponse> search(
            @RequestBody(required = false) @Parameter(description = "Фильтр для запроса") @Valid RoomSearchRequest searchRequest,
            Pageable pageable) {
        return roomService.search(searchRequest, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Получить помещение по id",
            description = "Возвращает статус 200 и найденное помещение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "404", description = "Помещение не найдено"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public RoomResponse getById(@PathVariable UUID id) {
        return roomService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Создать помещение",
            description = "Возвращает статус 200 и созданное помещение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Помещение создано"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public RoomResponse save(@RequestBody @Valid RoomCreateRequest createRequest) {
        return roomService.save(createRequest);
    }

    @PatchMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Обновить помещение",
            description = "Возвращает статус 200 и обновленное помещение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Помещение обновлено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Помещение не найдено"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public RoomResponse update(@PathVariable UUID id, @RequestBody @Valid RoomUpdateRequest updateRequest) {
        return roomService.update(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Удалить помещение",
            description = "Возвращает статус 200")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Помещение удалено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Помещение не найдено"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public void delete(@PathVariable UUID id) {
        roomService.delete(id);
    }
}
