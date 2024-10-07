package ru.irlix.booking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestCreate;
import ru.irlix.booking.entity.Booking;
import ru.irlix.booking.entity.BreakageRequest;
import ru.irlix.booking.entity.Office;
import ru.irlix.booking.entity.Role;
import ru.irlix.booking.entity.Room;
import ru.irlix.booking.entity.User;
import ru.irlix.booking.entity.Workplace;
import ru.irlix.booking.repository.BookingRepository;
import ru.irlix.booking.repository.BreakageRequestRepository;
import ru.irlix.booking.repository.OfficeRepository;
import ru.irlix.booking.repository.RoleRepository;
import ru.irlix.booking.repository.RoomRepository;
import ru.irlix.booking.repository.UserRepository;
import ru.irlix.booking.repository.WorkplaceRepository;
import ru.irlix.booking.security.config.PasswordEncoder;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName(value = "Тесты контроллера заявок о поломках")
class BreakageRequestControllerTest extends BaseIntegrationTest {

    private BreakageRequest testBreakageRequest;
    private Workplace testWorkplace;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private WorkplaceRepository workplaceRepository;

    @Autowired
    private BreakageRequestRepository breakageRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    public void setUp() {
        Office testOffice = Office.builder()
                .address("123 Main St, Springfield")
                .name("Head Office")
                .isDelete(false)
                .build();
        Office savedOffice = officeRepository.save(testOffice);

        Room testRoom = Room.builder()
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

        Role testUserRole = Role.builder()
                .id(UUID.fromString("15151515-1515-1515-1515-151515151515"))
                .name("USER")
                .build();
        Role savedRoleUser = roleRepository.save(testUserRole);

        Role testAdminRole = Role.builder()
                .id(UUID.fromString("14141414-1414-1414-1414-141414141414"))
                .name("ADMIN")
                .build();
        Role savedRoleAdmin = roleRepository.save(testAdminRole);

        User testUser = User.builder()
                .fio("Ignatiev Ignat Ignatievich")
                .phoneNumber("88003000400")
                .email("ignat@yandex.ru")
                .roles(Set.of(savedRoleUser))
                .password(new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'})
                .availableMinutesForBooking(120)
                .isDelete(false)
                .build();
        userRepository.save(testUser);

        User testAdmin = User.builder()
                .fio("Admin Admin Admin")
                .phoneNumber("+78002000600")
                .email("test.dev@gmail.com")
                .roles(Set.of(savedRoleAdmin))
                .password(passwordEncoder.passwordEncoder().encode("password123").toCharArray())
                .availableMinutesForBooking(120)
                .isDelete(false)
                .build();
        userRepository.save(testAdmin);

        Booking testBookingAdmin = Booking.builder()
                .bookingDateTime(LocalDateTime.now())
                .bookingStartDateTime(LocalDateTime.now())
                .bookingEndDateTime(LocalDateTime.now().plusHours(1))
                .bookingCancelDateTime(null)
                .cancelReason(null)
                .isBooked(true)
                .workplace(testWorkplace)
                .user(testAdmin)
                .build();
        bookingRepository.save(testBookingAdmin);

        testBreakageRequest = BreakageRequest.builder()
                .requestDateTime(LocalDateTime.now().withNano(0))
                .description("Test breakage 1")
                .isComplete(false)
                .isCanceled(false)
                .user(testUser)
                .workplace(testWorkplace)
                .build();
        breakageRequestRepository.save(testBreakageRequest);

        BreakageRequest testBreakageRequest2 = BreakageRequest.builder()
                .requestDateTime(LocalDateTime.now())
                .description("Test breakage 2")
                .isComplete(true)
                .isCanceled(false)
                .user(testUser)
                .workplace(testWorkplace)
                .build();
        breakageRequestRepository.save(testBreakageRequest2);

        BreakageRequest testBreakageRequest3 = BreakageRequest.builder()
                .requestDateTime(LocalDateTime.now())
                .description("Test breakage 3")
                .isComplete(false)
                .isCanceled(false)
                .user(testUser)
                .workplace(testWorkplace)
                .build();
        breakageRequestRepository.save(testBreakageRequest3);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на получение списка заявок для пользователя")
    @WithMockUser(value = "ignat@yandex.ru")
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
        UUID id = testBreakageRequest.getId();

        mockMvc.perform(get("/breakages/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestDateTime").value(LocalDateTime.now().withNano(0).toString()))
                .andExpect(jsonPath("$.description").value("Test breakage 1"))
                .andExpect(jsonPath("$.isComplete").value("false"))
                .andExpect(jsonPath("$.isCanceled").value("false"));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
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
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
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
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
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
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
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
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
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
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
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
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
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
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
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
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
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
    @Tag(value = "Позитивный")
    @WithMockUser(username = "test.dev@gmail.com", authorities = "ROLE_ADMIN")
    @DisplayName(value = "Позитивный тест на создание заявки о поломке")
    void save_success() throws Exception {
        UUID workplaceId = testWorkplace.getId();
        String body = getMapper().writeValueAsString(new BreakageRequestCreate("Create test", workplaceId));

        mockMvc.perform(post("/breakages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Create test"));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @WithMockUser(username = "test.dev@gmail.com", authorities = "ROLE_ADMIN")
    @DisplayName(value = "Негативный тест на создание заявки о поломке")
    void save_failed() throws Exception {
        String body = getMapper().writeValueAsString(new BreakageRequestCreate("Create test", UUID.randomUUID()));

        mockMvc.perform(post("/breakages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    @DisplayName(value = "Позитивный тест на обновление заявки о поломке")
    void update_success() throws Exception {
        UUID id = testBreakageRequest.getId();

        mockMvc.perform(patch("/breakages/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    @DisplayName(value = "Негативный тест на обновление заявки о поломке")
    void update_failed() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(patch("/breakages/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"test\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    @DisplayName(value = "Позитивный тест на удаление заявки о поломке")
    void delete_success() throws Exception {
        UUID id = testBreakageRequest.getId();
        mockMvc.perform(delete("/breakages/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/breakages/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isCanceled").value(true));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    @DisplayName(value = "Негативный тест на удаление заявки о поломке")
    void delete_failed() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/breakages/{id}", id))
                .andExpect(status().isOk());
    }
}
