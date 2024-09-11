package ru.irlix.booking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.irlix.booking.dto.workplace.WorkplaceCreateRequest;
import ru.irlix.booking.dto.workplace.WorkplaceResponse;
import ru.irlix.booking.dto.workplace.WorkplaceSearchRequest;
import ru.irlix.booking.dto.workplace.WorkplaceUpdateRequest;
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
@TestPropertySource("classpath:application-test.properties")
class WorkplaceControllerTest extends BaseIntegrationTest {

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение списка рабочих мест")
    void getAllTest_success() throws Exception {
        mockMvc.perform(get("/workplaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение рабочего места по id")
    void getByIdTest_success() throws Exception {
        UUID id = UUID.fromString("55555555-5555-5555-5555-555555555555");
        mockMvc.perform(get("/workplaces/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.description").value("Workplace near window"))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение рабочего места по id")
    void searchTest_success() throws Exception {
        WorkplaceResponse expected = new WorkplaceResponse(2, "Workplace with desk", false);
        String filter = getMapper().writeValueAsString(new WorkplaceSearchRequest(2, false, null));

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
    void saveTest_success() throws Exception {
        UUID roomId = UUID.fromString("33333333-3333-3333-3333-333333333333");
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
    void updateTest_success() throws Exception {
        UUID workplaceId = UUID.fromString("55555555-5555-5555-5555-555555555555");
        UUID roomId = UUID.fromString("44444444-4444-4444-4444-444444444444");

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
    void deleteTest_success() throws Exception {
        UUID id = UUID.fromString("55555555-5555-5555-5555-555555555555");

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
    void notFoundTest_notFound() throws Exception {
        mockMvc.perform(get("/workplaces/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}