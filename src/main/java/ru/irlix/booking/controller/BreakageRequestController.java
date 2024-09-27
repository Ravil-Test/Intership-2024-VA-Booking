package ru.irlix.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestCreate;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestUpdate;
import ru.irlix.booking.dto.breakagerequest.BreakageResponse;
import ru.irlix.booking.dto.breakagerequest.BreakageSearchRequest;
import ru.irlix.booking.service.BreakageRequestService;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/breakages")
@Tag(name = "Контроллер заявок о поломках")
public class BreakageRequestController {

    private final BreakageRequestService breakageRequestService;

    @GetMapping
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Получить список заявок о поломках",
            description = "Возвращает статус 200 и список заявок о поломках")
    public List<BreakageResponse> getAll() {
        return breakageRequestService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Получить заявку о поломке по id",
            description = "Возвращает статус 200 и заявку о поломке")
    public BreakageResponse getById(@PathVariable UUID id) {
        return breakageRequestService.getById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Получить список заявок о поломке оборудования",
            description = "Возвращает статус 200 и список заявок")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "404", description = "Поломок с таким фильтром не найдено")
    })
    public Page<BreakageResponse> search(@RequestBody(required = false) @Parameter(description = "Фильтр заявок")
                                         @Valid BreakageSearchRequest searchRequest,
                                         @RequestParam(name = "page", defaultValue = "0") @Min(value = 0,
                                                 message = "Страница не может быть отрицательным числом") int page,
                                         @RequestParam(name = "size", defaultValue = "10") @Min(value = 1,
                                                 message = "Число отображаемых объектов не может быть меньше 1") int size) {
        return breakageRequestService.search(PageRequest.of(page, size), searchRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Создать заявку о поломке",
            description = "Возвращает статус 201 и созданную заявку о поломке")
    public BreakageResponse createBreakageRequest(@RequestBody @Valid BreakageRequestCreate createRequest) {
        return breakageRequestService.save(createRequest);
    }

    @PatchMapping("/{id}")
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Обновить заявку о поломке",
            description = "Возвращает статус 200 и обновленную заявку о поломке")
    public BreakageResponse updateBreakageRequest(@PathVariable UUID id,
                                                  @RequestBody @Valid BreakageRequestUpdate updateRequest) {
        return breakageRequestService.update(id, updateRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Удалить заявку о поломке",
            description = "Возвращает статус 200 ")
    public void deleteBreakageRequest(@PathVariable UUID id) {
        breakageRequestService.delete(id);
    }
}