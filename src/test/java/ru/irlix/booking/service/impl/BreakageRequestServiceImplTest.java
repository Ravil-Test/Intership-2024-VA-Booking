package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestCreate;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestUpdate;
import ru.irlix.booking.dto.breakagerequest.BreakageResponse;
import ru.irlix.booking.service.BreakageRequestService;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "Тесты безнес-логики заявок о поломках")
@TestPropertySource("classpath:application-test.properties")
@Sql({
        "classpath:sql/init_breakageRequestTest.sql"
})
class BreakageRequestServiceImplTest extends BaseIntegrationTest {

    @Autowired
    private BreakageRequestService breakageService;

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на получение заявки по id")
    void getById_success() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-132435461234");

        BreakageResponse actualResponse = breakageService.getById(id);

        assertNotNull(actualResponse);
        assertEquals("Test breakage 1", actualResponse.description());
        assertFalse(actualResponse.isComplete());
        assertFalse(actualResponse.isCanceled());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Негативный тест на получение заявки по id")
    void getById_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> breakageService.getById(id));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на создание заявки о поломке")
    @Transactional(readOnly = true)
    void save_success() throws EntityNotFoundException {

        UUID workplaceId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        BreakageRequestCreate createRequest = new BreakageRequestCreate("Hello Kitty",
                workplaceId, userId);

        BreakageResponse savedResponse = breakageService.save(createRequest);

        assertNotNull(savedResponse);
        assertEquals(createRequest.description(), savedResponse.description());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на обновление заявки о поломке")
    @Transactional(readOnly = true)
    void update_success() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-132435461234");
        BreakageRequestUpdate updateRequest = new BreakageRequestUpdate("New description");

        BreakageResponse userResponse = breakageService.update(id, updateRequest);

        assertNotNull(userResponse);
        assertEquals(updateRequest.description(), userResponse.description());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Негативный тест на обновление заявки о поломке")
    void update_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        BreakageRequestUpdate updateRequest = new BreakageRequestUpdate("New description");

        assertThrows(EntityNotFoundException.class, () -> breakageService.update(id, updateRequest));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на удаление заявки о поломке")
    @Transactional(readOnly = true)
    void delete_success() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-132435461234");
        breakageService.delete(id);

        assertThrows(EntityNotFoundException.class, () -> breakageService.getById(id));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Негативный тест на удаление несуществующей заявки о поломке")
    @Transactional
    void delete_notFound() {
        UUID id = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> breakageService.delete(id));
    }
}