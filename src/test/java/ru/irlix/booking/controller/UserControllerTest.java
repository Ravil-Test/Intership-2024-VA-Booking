package ru.irlix.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName(value = "Тесты контроллера пользователей")
class UserControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на получение списка пользователей")
    void getAll() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на получение пользователя по id")
    void getById() throws Exception {
        UUID id = UUID.fromString("13131313-1313-1313-1313-131313131313");

        mockMvc.perform(get("/users/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fio").value("Петров Пётр Петрович"))
                .andExpect(jsonPath("$.phoneNumber").value("88002008888"))
                .andExpect(jsonPath("$.email").value("petrov.dev@gmail.com"))
                .andExpect(jsonPath("$.roles[0].name").value("USER"));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на создание пользователя")
    void create() throws Exception {

        UserCreateRequest userCreateRequest = new UserCreateRequest(
                "Александров Александ Александрович",
                "77007000700",
                "alexandrov.dev@gmail.com",
                "password1".toCharArray()
        );

        String jsonRequest = objectMapper.writeValueAsString(userCreateRequest);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(jsonRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fio").value("Александров Александ Александрович"))
                .andExpect(jsonPath("$.phoneNumber").value("77007000700"))
                .andExpect(jsonPath("$.email").value("alexandrov.dev@gmail.com"))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на обновление пользователя")
    void update() throws Exception {

        UUID id = UUID.fromString("12121212-1212-1212-1212-121212121212");

        UserCreateRequest userCreateRequest = new UserCreateRequest(
                "Васильев Василий Васильевич",
                "99009000900",
                "Vasya.dev@gmail.com",
                "password1112".toCharArray()
        );

        String jsonRequest = objectMapper.writeValueAsString(userCreateRequest);

        mockMvc.perform(patch("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(jsonRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fio").value("Васильев Василий Васильевич"))
                .andExpect(jsonPath("$.phoneNumber").value("99009000900"))
                .andExpect(jsonPath("$.email").value("Vasya.dev@gmail.com"))
                .andExpect(jsonPath("$.isDelete").value(false));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на удаление пользователя")
    void delete() throws Exception {
        UUID id = UUID.fromString("13131313-1313-1313-1313-131313131313");

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(jsonPath("$.isDelete").value(true));
    }
}