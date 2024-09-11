package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ru.irlix.booking.dto.workplace.WorkplaceCreateRequest;
import ru.irlix.booking.dto.workplace.WorkplaceResponse;
import ru.irlix.booking.dto.workplace.WorkplaceSearchRequest;
import ru.irlix.booking.dto.workplace.WorkplaceUpdateRequest;
import ru.irlix.booking.service.WorkplaceService;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName(value = "Тесты бизнес-логики рабочих мест")
@TestPropertySource("classpath:application-test.properties")
class WorkplaceServiceImplTest extends BaseIntegrationTest {

    @Autowired
    private WorkplaceService workplaceService;

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест получения рабочего места по id")
    void getById_success() {
        UUID id = UUID.fromString("55555555-5555-5555-5555-555555555555");

        WorkplaceResponse foundWorkplace = workplaceService.getById(id);

        assertNotNull(foundWorkplace);
        assertEquals(1, foundWorkplace.number());
        assertEquals("Workplace near window", foundWorkplace.description());
        assertFalse(foundWorkplace.isDelete());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест получения рабочего места по id")
    void getByIdTest_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> workplaceService.getById(id));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест получения списка рабочих мест")
    void getAll_success() {
        List<WorkplaceResponse> expectedResponse = List.of(
                new WorkplaceResponse(1, "Workplace near window", false),
                new WorkplaceResponse(2, "Workplace with desk", false));

        List<WorkplaceResponse> actualResponse = workplaceService.getAll();

        assertNotNull(actualResponse);
        assertFalse(actualResponse.isEmpty());
        assertTrue(actualResponse.stream().anyMatch(actual -> actual.equals(expectedResponse.get(0))));
        assertTrue(actualResponse.stream().anyMatch(actual -> actual.equals(expectedResponse.get(1))));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест получения страницы со списком рабочих мест по фильтру")
    void searchTest_success() {
        WorkplaceResponse expected = new WorkplaceResponse(1, "Workplace near window", false);

        WorkplaceSearchRequest filter = new WorkplaceSearchRequest(1, false, null);
        Pageable pageable = PageRequest.of(0, 10);

        List<WorkplaceResponse> actual = workplaceService.search(filter, pageable).getContent();

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertTrue(actual.stream().anyMatch(workplace -> workplace.equals(expected)));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест создания рабочего места")
    void saveTest_success() {
        WorkplaceCreateRequest createRequest = new WorkplaceCreateRequest(5, "Test create", UUID.fromString("44444444-4444-4444-4444-444444444444"));
        WorkplaceResponse expected = new WorkplaceResponse(5, "Test create", false);

        WorkplaceResponse createdWorkplace = workplaceService.save(createRequest);

        assertNotNull(createdWorkplace);
        assertEquals(expected, createdWorkplace);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест обновления рабочего места")
    void updateTest_success() {
        UUID id = UUID.fromString("66666666-6666-6666-6666-666666666666");
        WorkplaceUpdateRequest update = new WorkplaceUpdateRequest(10, "Test update", null);

        WorkplaceResponse expected = new WorkplaceResponse(10, "Test update", false);

        WorkplaceResponse updatedWorkplace = workplaceService.update(id, update);

        assertNotNull(updatedWorkplace);
        assertEquals(expected, updatedWorkplace);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест обновления рабочего места")
    void updateTest_notFound() {
        UUID id = UUID.randomUUID();
        WorkplaceUpdateRequest update = new WorkplaceUpdateRequest(10, "Test update", null);

        assertThrows(EntityNotFoundException.class, () -> workplaceService.update(id, update));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест удаления рабочего места по id")
    void delete_success() {
        UUID id = UUID.fromString("66666666-6666-6666-6666-666666666666");
        workplaceService.delete(id);

        assertTrue(workplaceService.getById(id).isDelete());
    }
}