package ru.irlix.booking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
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
import ru.irlix.booking.util.BaseIntegrationTest;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName(value = "Тесты контроллера рабочих мест")
class WorkplaceControllerTest extends BaseIntegrationTest {

    private Room testRoom;

    private Workplace testWorkplace;

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
    }

    @Test
    @WithMockUser
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение списка рабочих мест")
    void getAllTest_success() throws Exception {
        mockMvc.perform(get("/workplaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение рабочего места по id")
    void getByIdTest_success() throws Exception {
        UUID id = testWorkplace.getId();

        mockMvc.perform(get("/workplaces/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.description").value("Workplace near window"))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    @WithMockUser
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение рабочего места по id")
    void searchTest_success() throws Exception {
        WorkplaceResponse expected = new WorkplaceResponse(1, "Workplace near window", false);
        String filter = getMapper().writeValueAsString(new WorkplaceSearchRequest(1, false, null));

        MvcResult mvcResult = mockMvc.perform(get("/workplaces/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filter))
                .andExpect(status().isOk())
                .andReturn();

        String actualContent = mvcResult.getResponse().getContentAsString();
        List<WorkplaceResponse> actualResponse = getMapper().readValue(
                getMapper().readTree(actualContent).get("content").toString(),
                new TypeReference<>() {
                }
        );

        Assertions.assertTrue(actualResponse.stream().anyMatch(content -> content.equals(expected)));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на создание рабочего места")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    void saveTest_success() throws Exception {
        UUID roomId = testRoom.getId();
        WorkplaceCreateRequest createRequest = new WorkplaceCreateRequest(5, "Create test description", roomId);
        String jsonCreateRequest = getMapper().writeValueAsString(createRequest);

        mockMvc.perform(post("/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value(5))
                .andExpect(jsonPath("$.description").value("Create test description"));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на обновление рабочего места")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    void updateTest_success() throws Exception {
        UUID workplaceId = testWorkplace.getId();
        UUID roomId = testRoom.getId();

        WorkplaceUpdateRequest updateRequest = new WorkplaceUpdateRequest(3, "Update test description", roomId);
        String jsonUpdateRequest = getMapper().writeValueAsString(updateRequest);

        mockMvc.perform(patch("/workplaces/{id}", workplaceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(3))
                .andExpect(jsonPath("$.description").value("Update test description"));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на удаление рабочего места")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    void deleteTest_success() throws Exception {
        UUID id = testWorkplace.getId();

        mockMvc.perform(delete("/workplaces/{id}", id))
                .andExpect(status().isOk());

        ResultActions perform = mockMvc.perform(get("/workplaces/{id}", id));
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDelete").value(true));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест получение рабочего места по id")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    void notFoundTest_notFound() throws Exception {
        mockMvc.perform(get("/workplaces/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}