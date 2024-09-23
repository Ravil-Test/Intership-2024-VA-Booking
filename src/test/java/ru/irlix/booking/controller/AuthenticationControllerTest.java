package ru.irlix.booking.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.irlix.booking.dto.user.AuthenticationUserRequest;
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.util.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName(value = "Тесты контроллера аутентификации")
class AuthenticationControllerTest extends BaseIntegrationTest {

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на регистрацию")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    void create_success() throws Exception {
        char[] password = "password".toCharArray();
        String jsonCreateRequest = getMapper().writeValueAsString
                (new UserCreateRequest("Иванов Иван Иванович", "+7951256891", "test@gmail.com", password));

        mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fio").value("Иванов Иван Иванович"))
                .andExpect(jsonPath("$.phoneNumber").value("+7951256891"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на регистрацию")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    void create_fail() throws Exception {
        char[] password = "password".toCharArray();
        String jsonCreateRequest = getMapper().writeValueAsString
                (new UserCreateRequest("Иванов Иван Иванович", "+7951w56891", "test@gmailcom", password));

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на авторизацию по номеру телефона")
    void login_by_phoneNumber_success() throws Exception {
        char[] password = "password123".toCharArray();

        String authRequest = getMapper().writeValueAsString(new AuthenticationUserRequest("+78002000600", password));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на авторизацию по email адресу")
    void login_by_email_success() throws Exception {
        char[] password = "password123".toCharArray();

        String authRequest = getMapper().writeValueAsString(new AuthenticationUserRequest("test.dev@gmail.com", password));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на авторизацию по номеру телефона")
    void login_by_phoneNumber_fail() throws Exception {
        char[] password = "password123".toCharArray();

        String authRequest = getMapper().writeValueAsString(new AuthenticationUserRequest("+7951256893", password));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на авторизацию по email адресу")
    void login_by_email_fail() throws Exception {
        char[] password = "password123".toCharArray();

        String authRequest = getMapper().writeValueAsString(new AuthenticationUserRequest("test1@gmail.com", password));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequest))
                .andExpect(status().isNotFound());
    }
}