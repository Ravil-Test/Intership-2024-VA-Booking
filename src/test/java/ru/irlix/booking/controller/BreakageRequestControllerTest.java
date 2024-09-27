package ru.irlix.booking.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName(value = "Тесты контроллера заявок о поломках")
@TestPropertySource("classpath:application-test.properties")
@Sql({
        "classpath:sql/init_breakageRequestTest.sql"
})
class BreakageRequestControllerTest extends BaseIntegrationTest {

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на получение списка заявок")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    void getAll() throws Exception {
        mockMvc.perform(get("/breakages"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на получение заявки по id")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    void getById() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-132435461234");

        mockMvc.perform(get("/breakages/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestDateTime").value("2024-09-23T15:03:22.611645"))
                .andExpect(jsonPath("$.description").value("Test breakage 1"))
                .andExpect(jsonPath("$.isComplete").value("false"))
                .andExpect(jsonPath("$.isCanceled").value("false"));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест с корректными параметрами, проверяющий пагинацию")
    void getAllPagination_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/breakages/search")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Негативный тест с некорректными параметрами, проверяющий пагинацию")
    void getAllPagination_notFound() throws Exception {

        mockMvc.perform(get("/breakages/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "-1")
                        .param("size", "0"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName("Позитивный тест поиска заявок по описанию")
    void searchDescription_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/breakages/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(3))
                .andExpect(jsonPath("$.content[0].description").value("Test breakage 1"))
                .andExpect(jsonPath("$.content[1].description").value("Test breakage 2"))
                .andExpect(jsonPath("$.content[2].description").value("Test breakage 3"));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName("Негативный тест поиск заявки по названию")
    void search_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/breakages/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"NoNameDescription\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(0));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName("Позитивный тест поиска заявок по статусу выполнения")
    void searchCompleted_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/breakages/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isComplete\": \"true\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content[0].description").value("Test breakage 2"));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName("Позитивный тест поиска заявок по рабочему месту")
    void searchWorkplace_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/breakages/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workplace\": \"55555555-5555-5555-5555-555555555555\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(3))
                .andExpect(jsonPath("$.content[0].description").value("Test breakage 1"))
                .andExpect(jsonPath("$.content[1].description").value("Test breakage 2"))
                .andExpect(jsonPath("$.content[2].description").value("Test breakage 3"));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName("Негативный тест поиска заявок по рабочему месту")
    void searchWorkplace_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/breakages/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"workplace_id\": \"09090909-0000-0000-0000-555555555555\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(0));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName("Позитивный тест поиска заявок по имени пользователя")
    void searchUser_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/breakages/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user\": \"42424242-4242-4242-4242-424242424242\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(3))
                .andExpect(jsonPath("$.content[0].description").value("Test breakage 1"))
                .andExpect(jsonPath("$.content[1].description").value("Test breakage 2"))
                .andExpect(jsonPath("$.content[2].description").value("Test breakage 3"));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName("Негативный тест поиска заявок по имени пользователя")
    void searchUser_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/breakages/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user_id\": \"42424242-4242-4242-4242-000000000111\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(0));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Негативный тест на создание заявки о поломке")
    void save_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/breakages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"null\", "
                                + "\"workplace_id\": \"null\", "
                                + "\"user_id\": \"null\"}"))
                .andExpect(status().isBadRequest());
    }
}
