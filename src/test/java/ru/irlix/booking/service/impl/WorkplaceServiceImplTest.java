package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.irlix.booking.dto.workplace.WorkplaceCreateRequest;
import ru.irlix.booking.dto.workplace.WorkplaceResponse;
import ru.irlix.booking.dto.workplace.WorkplaceSearchRequest;
import ru.irlix.booking.dto.workplace.WorkplaceUpdateRequest;
import ru.irlix.booking.entity.Office;
import ru.irlix.booking.entity.Room;
import ru.irlix.booking.entity.Workplace;
import ru.irlix.booking.repository.OfficeRepository;
import ru.irlix.booking.repository.RoomRepository;
import ru.irlix.booking.repository.WorkplaceRepository;
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
class WorkplaceServiceImplTest extends BaseIntegrationTest {

    private Room testRoom;

    private Workplace testWorkplace;

    @Autowired
    private WorkplaceService workplaceService;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private WorkplaceRepository workplaceRepository;

    @BeforeEach
    public void setUp() {
        Office testOffice = Office.builder()
                .address("123 Main St, Springfield")
                .name("Head Office")
                .isDelete(false)
                .build();
        Office savedOffice = officeRepository.save(testOffice);

        testRoom = Room.builder()
                .id(UUID.randomUUID())
                .name("Small meeting room")
                .floorNumber((short) 3)
                .roomNumber((short) 15)
                .isDelete(false)
                .office(savedOffice)
                .build();
        Room savedRoom = roomRepository.save(testRoom);
        testRoom.setId(savedRoom.getId());

        testWorkplace = Workplace.builder()
                .number(1)
                .description("Workplace near window")
                .isDelete(false)
                .room(savedRoom)
                .build();
        workplaceRepository.save(testWorkplace);

        Workplace testWorkplace2 = Workplace.builder()
                .number(2)
                .description("Workplace with desk")
                .isDelete(false)
                .room(savedRoom)
                .build();
        workplaceRepository.save(testWorkplace2);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест получения рабочего места по id")
    void getById_success() {
        UUID id = testWorkplace.getId();

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
        WorkplaceCreateRequest createRequest = new WorkplaceCreateRequest(5, "Test create", testRoom.getId());
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
        UUID id = testWorkplace.getId();
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
        UUID id = testWorkplace.getId();
        workplaceService.delete(id);

        assertTrue(workplaceService.getById(id).isDelete());
    }
}