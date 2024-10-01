package ru.irlix.booking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.entity.Role;
import ru.irlix.booking.entity.User;
import ru.irlix.booking.repository.RoleRepository;
import ru.irlix.booking.repository.UserRepository;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName(value = "Тесты контроллера пользователей")
class UserControllerTest extends BaseIntegrationTest {

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

        testUser = User.builder()
                .fio("Ignatiev Ignat Ignatievich")
                .phoneNumber("88003000400")
                .email("ignat@yandex.ru")
                .roles(Set.of(savedRole))
                .password(new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'})
                .availableMinutesForBooking(120)
                .isDelete(false)
                .build();
        userRepository.save(testUser);

        User testUser2 = User.builder()
                .fio("Petrov Ignat Petrovich")
                .phoneNumber("88004000500")
                .email("petr@yandex.ru")
                .roles(Set.of(savedRole))
                .password(new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'})
                .availableMinutesForBooking(120)
                .isDelete(true)
                .build();
        userRepository.save(testUser2);

        User testUser3 = User.builder()
                .fio("Sidorov Sergey Ivanovich")
                .phoneNumber("88005000600")
                .email("sidr@yandex.ru")
                .roles(Set.of(savedRole))
                .password(new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'})
                .availableMinutesForBooking(120)
                .isDelete(false)
                .build();
        userRepository.save(testUser3);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
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
    @WithMockUser(authorities = "ROLE_USER")
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на получение пользователя по id")
    void getById() throws Exception {
        UUID id = testUser.getId();

        mockMvc.perform(get("/users/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fio").value("Ignatiev Ignat Ignatievich"))
                .andExpect(jsonPath("$.phoneNumber").value("88003000400"))
                .andExpect(jsonPath("$.email").value("ignat@yandex.ru"))
                .andExpect(jsonPath("$.roles[0].name").value("USER"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест с корректными параметрами, проверяющий пагинацию")
    void getAllPagination_success() throws Exception {
        String pageRequest = getMapper().writeValueAsString(PageRequest.of(0, 10));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pageRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Негативный тест с некорректными параметрами, проверяющий пагинацию")
    void getAllPagination_notFound() throws Exception {
        mockMvc.perform(get("/users/search")
                        .param("page", "-1")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName("Позитивный тест поиска и сортировки пользователей по инициалам и is_delete")
    void searchFioUnique_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fio\": \"Ignat\", \"isDelete\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1))
                .andExpect(jsonPath("$.content.[0].fio").value("Ignatiev Ignat Ignatievich"));

    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName("Позитивный тест поиска и сортировки пользователей по инициалам")
    void searchFioList_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fio\": \"Ignat\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content.[0].fio").value("Ignatiev Ignat Ignatievich"))
                .andExpect(jsonPath("$.content.[1].fio").value("Petrov Ignat Petrovich"))
                .andExpect(jsonPath("$.content.[0].isDelete").value(false))
                .andExpect(jsonPath("$.content.[1].isDelete").value(true));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName("Негативный тест пользователь не найден")
    void search_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fio\": \"NoNameUser\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(0));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName("Позитивный тест поиска пользователей по статусу is_delete")
    void searchIsDelete_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isDelete\": false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].fio").value("Ignatiev Ignat Ignatievich"))
                .andExpect(jsonPath("$.content[1].fio").value("Sidorov Sergey Ivanovich"));
    }

    @Test
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
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

        String jsonRequest = getMapper().writeValueAsString(userCreateRequest);

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
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на обновление пользователя")
    void update() throws Exception {
        UUID id = testUser.getId();

        UserCreateRequest userCreateRequest = new UserCreateRequest(
                "Васильев Василий Васильевич",
                "99009000900",
                "Vasya.dev@gmail.com",
                "password1112".toCharArray()
        );

        String jsonRequest = getMapper().writeValueAsString(userCreateRequest);

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
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на удаление пользователя")
    void delete() throws Exception {
        UUID id = testUser.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(jsonPath("$.isDelete").value(true));
    }
}