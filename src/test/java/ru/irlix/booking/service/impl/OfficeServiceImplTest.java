package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.irlix.booking.dto.office.OfficeCreateRequest;
import ru.irlix.booking.dto.office.OfficeResponse;
import ru.irlix.booking.dto.office.OfficeSearchRequest;
import ru.irlix.booking.dto.office.OfficeUpdateRequest;
import ru.irlix.booking.entity.Office;
import ru.irlix.booking.repository.OfficeRepository;
import ru.irlix.booking.service.OfficeService;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName(value = "Тесты бизнес-логики офисов")
class OfficeServiceImplTest extends BaseIntegrationTest {

    private Office testOffice;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private OfficeRepository officeRepository;

    @BeforeEach
    public void setUp() {
        testOffice = Office.builder()
                .address("123 Main St, Springfield")
                .name("Head Office")
                .isDelete(false)
                .build();
        officeRepository.save(testOffice);

        Office testOffice2 = Office.builder()
                .address("123 Elm St, Downtown")
                .name("Main Office")
                .isDelete(false)
                .build();
        officeRepository.save(testOffice2);

        Office testOffice3 = Office.builder()
                .address("Update test address")
                .name("Update test name")
                .isDelete(false)
                .build();
        officeRepository.save(testOffice3);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получения офиса по id")
    void getById_success() {
        UUID id = testOffice.getId();
        OfficeResponse expected = new OfficeResponse("123 Main St, Springfield", "Head Office", false);
        OfficeResponse actual = officeService.getById(id);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на получения офиса по id")
    void getById_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        Assertions.assertThrows(EntityNotFoundException.class, () -> officeService.getById(id));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение списка офисов")
    void getAll_success() {
        List<OfficeResponse> expectedResponse = List.of(
                new OfficeResponse("123 Main St, Springfield", "Head Office", false),
                new OfficeResponse("123 Elm St, Downtown", "Main Office", false),
                new OfficeResponse("Update test address", "Update test name", false));

        List<OfficeResponse> actualResponse = officeService.getAll();

        Assertions.assertNotNull(actualResponse);
        Assertions.assertTrue(actualResponse.stream().anyMatch(response -> response.equals(expectedResponse.get(0))));
        Assertions.assertTrue(actualResponse.stream().anyMatch(response -> response.equals(expectedResponse.get(1))));
        Assertions.assertTrue(actualResponse.stream().anyMatch(response -> response.equals(expectedResponse.get(2))));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение страницы со списком офисов")
    void search_success() {
        OfficeSearchRequest filter = new OfficeSearchRequest("123 Main St, Springfield", null, null);
        List<OfficeResponse> expectedResponse = List.of(
                new OfficeResponse("123 Main St, Springfield", "Head Office", false));

        Pageable pageRequest = PageRequest.of(0, 10);
        List<OfficeResponse> actualResponse = officeService.search(filter, pageRequest).getContent();

        Assertions.assertNotNull(actualResponse);
        Assertions.assertFalse(actualResponse.isEmpty());
        Assertions.assertTrue(actualResponse.stream().anyMatch(response -> response.equals(expectedResponse.get(0))));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на создание офиса")
    void save() {
        OfficeResponse expectedResponse = new OfficeResponse("Test create address", "Test create name", false);
        OfficeCreateRequest createRequest = new OfficeCreateRequest("Test create address", "Test create name");

        OfficeResponse saved = officeService.save(createRequest);

        Assertions.assertNotNull(saved);
        Assertions.assertEquals(expectedResponse, saved);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на обновление офиса")
    void update_success() {
        UUID id = testOffice.getId();
        OfficeUpdateRequest updateRequest = new OfficeUpdateRequest("Test update address", "Test update name");
        OfficeResponse expectedResponse = new OfficeResponse("Test update address", "Test update name", false);

        OfficeResponse actualResponse = officeService.update(id, updateRequest);

        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на обновление офиса")
    void update_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        OfficeUpdateRequest updateRequest = new OfficeUpdateRequest("Test update address", "Test update name");
        assertThrows(EntityNotFoundException.class, () -> officeService.update(id, updateRequest));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на удаление офиса")
    void delete_success() {
        UUID id = testOffice.getId();
        officeService.delete(id);
        OfficeResponse found = officeService.getById(id);
        Assertions.assertNotNull(found);
        Assertions.assertTrue(found.isDelete());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение офиса проверкой на null")
    void getOfficeById_success() {
        UUID id = testOffice.getId();

        Office office = officeService.getOfficeById(id);

        Assertions.assertNotNull(office);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на получение офиса  проверкой на null")
    void getOfficeById_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        Assertions.assertThrows(EntityNotFoundException.class, () -> officeService.getOfficeById(id));
    }
}