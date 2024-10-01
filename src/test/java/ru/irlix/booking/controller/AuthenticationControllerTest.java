package ru.irlix.booking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.irlix.booking.dto.user.AuthenticationUserRequest;
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.entity.Role;
import ru.irlix.booking.entity.User;
import ru.irlix.booking.repository.RoleRepository;
import ru.irlix.booking.repository.UserRepository;
import ru.irlix.booking.security.config.PasswordEncoder;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName(value = "Тесты контроллера аутентификации")
class AuthenticationControllerTest extends BaseIntegrationTest {

    private User testUser;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        Role testRole = Role.builder()
                .id(UUID.fromString("15151515-1515-1515-1515-151515151515"))
                .name("USER")
                .build();
        Role savedRole = roleRepository.save(testRole);

        PasswordEncoder passwordEncoder = new PasswordEncoder();

        testUser = User.builder()
                .fio("Ignatiev Ignat Ignatievich")
                .phoneNumber("+78002000600")
                .email("test.dev@gmail.com")
                .roles(Set.of(savedRole))
                .password(passwordEncoder.passwordEncoder().encode("password").toCharArray())
                .availableMinutesForBooking(120)
                .isDelete(false)
                .build();
        userRepository.save(testUser);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на регистрацию")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    void create_fail() throws Exception {
        char[] password = "password".toCharArray();

        String jsonCreateRequest = getMapper().writeValueAsString
                (new UserCreateRequest("Ignatiev Ignat Ignatievich", "+78002000600", "test.dev@gmail.com", password));

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
        char[] password = "password".toCharArray();
        String phoneNumber = testUser.getPhoneNumber();

        String authRequest = getMapper().writeValueAsString(new AuthenticationUserRequest(phoneNumber, password));

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
        char[] password = "password".toCharArray();
        String email = testUser.getEmail();

        String authRequest = getMapper().writeValueAsString(new AuthenticationUserRequest(email, password));

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
        char[] password = "password".toCharArray();

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
        char[] password = "password".toCharArray();

        String authRequest = getMapper().writeValueAsString(new AuthenticationUserRequest("test1@gmail.com", password));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequest))
                .andExpect(status().isNotFound());
    }
}