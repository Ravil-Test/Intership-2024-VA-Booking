package ru.irlix.booking.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.irlix.booking.dto.room.RoomCreateRequest;
import ru.irlix.booking.dto.room.RoomUpdateRequest;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName(value = "Тесты контроллера помещений")
class RoomControllerTest extends BaseIntegrationTest {

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение списка помещений")
    void getAllTest_success() throws Exception {
        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение помещения по id")
    void getByIdTest_success() throws Exception {
        UUID id = UUID.fromString("44444444-4444-4444-4444-444444444444");

        mockMvc.perform(get("/rooms/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Малый переговорный зал"))
                .andExpect(jsonPath("$.floorNumber").value(3))
                .andExpect(jsonPath("$.roomNumber").value(15))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на создание помещения")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    void createTest_success() throws Exception {
        UUID officeId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        RoomCreateRequest createRequest = new RoomCreateRequest("Create test name", (short) 14, (short) 15, officeId);
        String jsonCreateRequest = getMapper().writeValueAsString(createRequest);

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Create test name"))
                .andExpect(jsonPath("$.floorNumber").value(14))
                .andExpect(jsonPath("$.roomNumber").value(15))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на обновление помещения")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    void updateTest_success() throws Exception {
        UUID roomId = UUID.fromString("55555555-5555-5555-5555-555555555555");
        UUID officeId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        RoomUpdateRequest updateRequest = new RoomUpdateRequest("Update test name", (short) 14, (short) 15, officeId);
        String jsonUpdateRequest = getMapper().writeValueAsString(updateRequest);

        mockMvc.perform(patch("/rooms/{id}", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Update test name"))
                .andExpect(jsonPath("$.floorNumber").value(14))
                .andExpect(jsonPath("$.roomNumber").value(15));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на удаление помещения")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    void deleteTest_success() throws Exception {
        UUID id = UUID.fromString("66666666-6666-6666-6666-666666666666");

        mockMvc.perform(MockMvcRequestBuilders.delete("/rooms/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rooms/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDelete").value(true));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест получение помещения по id")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    void notFoundCheck_notFound() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/rooms/{id}", id))
                .andExpect(status().isNotFound());
    }
}