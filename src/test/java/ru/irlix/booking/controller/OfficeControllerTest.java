package ru.irlix.booking.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import ru.irlix.booking.dto.office.OfficeCreateRequest;
import ru.irlix.booking.dto.office.OfficeResponse;
import ru.irlix.booking.dto.office.OfficeUpdateRequest;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName(value = "Тесты контроллера офисов")
class OfficeControllerTest extends BaseIntegrationTest {

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение списка офисов")
    void getAllTest_success() throws Exception {
        mockMvc.perform(get("/offices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение страницы со списком офисов")
    void getOfficePageTest_success() throws Exception {
        OfficeResponse expectedOffice = new OfficeResponse("123 Main St, Springfield", "Head Office", false);

        MvcResult mvcResult = mockMvc.perform(get("/offices/find")
                        .param("name", "Head Office")
                        .param("isDelete", "false")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name"))
                .andExpect(status().isOk())
                .andReturn();

        String contentResponse = mvcResult.getResponse().getContentAsString();

        List<OfficeResponse> actualResponse = getMapper().readValue(getMapper().readTree(contentResponse).get("content").toString(),
                new TypeReference<>() {
                });

        Assertions.assertFalse(actualResponse.isEmpty());
        Assertions.assertEquals(expectedOffice, actualResponse.get(0));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение офиса на id")
    void getOfficeByIdTest_success() throws Exception {
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");

        mockMvc.perform(get("/offices/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Head Office"))
                .andExpect(jsonPath("$.address").value("123 Main St, Springfield"))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на создание офиса")
    void createOfficeTest_success() throws Exception {
        OfficeCreateRequest createRequest = new OfficeCreateRequest("Test address", "Test name");
        String jsonCreateRequest = getMapper().writeValueAsString(createRequest);

        mockMvc.perform(post("/offices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test name"))
                .andExpect(jsonPath("$.address").value("Test address"))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на обновление офиса")
    void updateOfficeTest_success() throws Exception {
        UUID id = UUID.fromString("33333333-3333-3333-3333-333333333333");

        OfficeUpdateRequest updateRequest = new OfficeUpdateRequest("Update test address", "Update test name");
        String jsonUpdateRequest = getMapper().writeValueAsString(updateRequest);

        mockMvc.perform(patch("/offices/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Update test name"))
                .andExpect(jsonPath("$.address").value("Update test address"))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на обновление офиса")
    void updateOfficeTest_notFound() throws Exception {
        UUID id = UUID.randomUUID();
        String updateRequest = getMapper().writeValueAsString(new OfficeUpdateRequest("Update test address", "Update test name"));

        mockMvc.perform(patch("/offices/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на удаление офиса")
    void deleteOfficeTest_success() throws Exception {
        UUID id = UUID.fromString("22222222-2222-2222-2222-222222222222");

        mockMvc.perform(delete("/offices/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/offices/{id}", id))
                .andExpect(jsonPath("$.isDelete").value(true));
    }
}