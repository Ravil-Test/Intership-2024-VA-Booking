package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.irlix.booking.dto.room.RoomCreateRequest;
import ru.irlix.booking.dto.room.RoomResponse;
import ru.irlix.booking.dto.room.RoomSearchRequest;
import ru.irlix.booking.dto.room.RoomUpdateRequest;
import ru.irlix.booking.entity.Office;
import ru.irlix.booking.entity.Room;
import ru.irlix.booking.repository.OfficeRepository;
import ru.irlix.booking.repository.RoomRepository;
import ru.irlix.booking.service.RoomService;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName(value = "Тесты бизнес-логики помещений")
class RoomServiceImplTest extends BaseIntegrationTest {

    private Room testRoom;

    private Office testOffice;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @BeforeEach
    public void setUp() {
        testOffice = Office.builder()
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

        Room testRoom2 = Room.builder()
                .id(UUID.randomUUID())
                .name("Large conference room")
                .floorNumber((short) 2)
                .roomNumber((short) 10)
                .isDelete(false)
                .office(savedOffice)
                .build();
        roomRepository.save(testRoom2);

        Room testRoom3 = Room.builder()
                .id(UUID.randomUUID())
                .name("Кабинет для backend разработки")
                .floorNumber((short) 1)
                .roomNumber((short) 4)
                .isDelete(false)
                .office(savedOffice)
                .build();
        roomRepository.save(testRoom3);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получения помещения по id")
    void getByIdTest_success() {
        UUID id = testRoom.getId();

        RoomResponse actualResponse = roomService.getById(id);

        assertNotNull(actualResponse);
        assertEquals("Small meeting room", actualResponse.name());
        assertEquals((short) 3, actualResponse.floorNumber());
        assertEquals((short) 15, actualResponse.roomNumber());
        assertFalse(actualResponse.isDelete());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на получения помещения по id")
    void getByIdTest_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> roomService.getById(id));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение списка помещений")
    void getAllTest_success() {
        List<RoomResponse> expectedResponseList = List.of(
                new RoomResponse("Small meeting room", (short) 3, (short) 15, false),
                new RoomResponse("Large conference room", (short) 2, (short) 10, false),
                new RoomResponse("Кабинет для backend разработки", (short) 1, (short) 4, false));
        List<RoomResponse> actualResponseList = roomService.getAll();

        assertEquals(expectedResponseList.size(), actualResponseList.size());
        assertFalse(actualResponseList.isEmpty());
        assertTrue(actualResponseList.stream().anyMatch(actualResponse -> actualResponse.equals(expectedResponseList.get(0))));
        assertTrue(actualResponseList.stream().anyMatch(actualResponse -> actualResponse.equals(expectedResponseList.get(1))));
        assertTrue(actualResponseList.stream().anyMatch(actualResponse -> actualResponse.equals(expectedResponseList.get(2))));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение страницы со списком помещений")
    void getRoomWithFilterTest_success() {
        RoomResponse expectedResponse = new RoomResponse("Small meeting room", (short) 3, (short) 15, false);
        RoomSearchRequest searchRequest = new RoomSearchRequest("Small meeting room", false, (short) 3, (short) 15, null);
        Pageable pageable = PageRequest.of(0, 10);

        Page<RoomResponse> roomPage = roomService.search(searchRequest, pageable);
        List<RoomResponse> actualResponse = roomPage.getContent();

        assertNotNull(actualResponse);
        assertEquals("Small meeting room", actualResponse.get(0).name());
        assertTrue(actualResponse.stream().anyMatch(actual -> actual.equals(expectedResponse)));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение страницы со списком помещений без фильтров")
    void getRoomWithoutFilterTest_success() {
        List<RoomResponse> expectedResponseList = List.of(
                new RoomResponse("Small meeting room", (short) 3, (short) 15, false),
                new RoomResponse("Large conference room", (short) 2, (short) 10, false));

        RoomSearchRequest filter = new RoomSearchRequest(null, null, null, null, null);
        Pageable pageable = PageRequest.of(0, 10);

        Page<RoomResponse> roomPage = roomService.search(filter, pageable);
        List<RoomResponse> actualContent = roomPage.getContent();
        assertNotNull(actualContent);
        assertFalse(actualContent.isEmpty());
        assertTrue(actualContent.stream().anyMatch(actualResponse -> actualResponse.equals(expectedResponseList.get(0))));
        assertTrue(actualContent.stream().anyMatch(actualResponse -> actualResponse.equals(expectedResponseList.get(1))));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на получение страницы со списком помещений с некорректным фильтром")
    void getRoomWithFilterTest_notFound() throws EntityNotFoundException {
        RoomSearchRequest filter = new RoomSearchRequest("Not found test", null, null, null, null);
        Pageable pageable = PageRequest.of(0, 10);

        Assertions.assertThrows(EntityNotFoundException.class, () -> roomService.search(filter, pageable));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на создание помещения")
    void saveRoomTest_success() {
        RoomCreateRequest createRequest = new RoomCreateRequest("Room A", (short) 2, (short) 202,
                testOffice.getId());

        RoomResponse savedRoom = roomService.save(createRequest);

        assertNotNull(savedRoom);
        assertEquals(createRequest.name(), savedRoom.name());
        assertEquals(createRequest.roomNumber(), savedRoom.roomNumber());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на обновление помещения")
    void updateTest_success() {
        UUID roomId = testRoom.getId();
        RoomUpdateRequest updateRequest = new RoomUpdateRequest("Update room", (short) 2, (short) 202,
                null);

        RoomResponse updatedRoom = roomService.update(roomId, updateRequest);

        assertNotNull(updatedRoom);
        assertEquals(updateRequest.name(), updatedRoom.name());
        assertEquals(updateRequest.roomNumber(), updatedRoom.roomNumber());
        assertEquals(updateRequest.floorNumber(), updatedRoom.floorNumber());
        assertFalse(updatedRoom.isDelete());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на создание помещения")
    void updateTest_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        RoomUpdateRequest updateRequest = new RoomUpdateRequest("Update room", (short) 2, (short) 202,
                null);

        assertThrows(EntityNotFoundException.class, () -> roomService.update(id, updateRequest));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на удаление помещения")
    void deleteRoomTest_success() {
        UUID id = testRoom.getId();
        roomService.delete(id);

        Optional<Room> deletedRoom = roomRepository.findById(id);
        assertTrue(deletedRoom.isPresent());
        assertTrue(deletedRoom.get().isDelete());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на удаление помещения")
    void deleteRoomTest_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> roomService.delete(id));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на получение помещения по id с проверкой на null")
    void getRoomByIdTest_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> roomService.getRoomById(id));
    }
}