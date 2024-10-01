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
import ru.irlix.booking.dto.booking.BookingCreateRequest;
import ru.irlix.booking.entity.Booking;
import ru.irlix.booking.entity.Office;
import ru.irlix.booking.entity.Room;
import ru.irlix.booking.entity.User;
import ru.irlix.booking.entity.Workplace;
import ru.irlix.booking.repository.BookingRepository;
import ru.irlix.booking.repository.OfficeRepository;
import ru.irlix.booking.repository.RoomRepository;
import ru.irlix.booking.repository.UserRepository;
import ru.irlix.booking.repository.WorkplaceRepository;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName(value = "Тесты контроллера бронирования>")
class BookingControllerTest extends BaseIntegrationTest {

    private User testUser;

    private Workplace testWorkplace;

    private Booking testBooking;

    private Booking testBookingCancel;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private WorkplaceRepository workplaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    public void setUp() {
        Office office = Office.builder()
                .address("123 Main St, Springfield")
                .name("Head Office")
                .isDelete(false)
                .build();
        Office savedOffice = officeRepository.save(office);

        Room room = Room.builder()
                .id(UUID.randomUUID())
                .name("Small meeting room")
                .floorNumber((short) 3)
                .roomNumber((short) 15)
                .isDelete(false)
                .office(savedOffice)
                .build();
        Room savedRoom = roomRepository.save(room);

        testWorkplace = Workplace.builder()
                .number(1)
                .description("Workplace near window")
                .isDelete(false)
                .room(savedRoom)
                .build();
        Workplace savedWorkplace = workplaceRepository.save(testWorkplace);

        testUser = User.builder()
                .fio("Sidorov Ivan Ivanovich")
                .phoneNumber("88002000600")
                .email("sidorov.dev@gmail.com")
                .password(new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'})
                .availableMinutesForBooking(120)
                .isDelete(false)
                .build();
        User savedUser = userRepository.save(testUser);

        testBooking = Booking.builder()
                .bookingDateTime(LocalDateTime.now().withNano(0))
                .bookingStartDateTime(LocalDateTime.now().withNano(0))
                .bookingEndDateTime(LocalDateTime.now().withNano(0))
                .bookingCancelDateTime(LocalDateTime.now().withNano(0))
                .cancelReason("Reason #1")
                .isBooked(false)
                .user(savedUser)
                .workplace(savedWorkplace)
                .build();
        bookingRepository.save(testBooking);

        testBookingCancel = Booking.builder()
                .bookingDateTime(LocalDateTime.now().withNano(0))
                .bookingStartDateTime(LocalDateTime.now().withNano(0))
                .bookingEndDateTime(LocalDateTime.now().withNano(0))
                .cancelReason("")
                .user(savedUser)
                .workplace(savedWorkplace)
                .build();
        bookingRepository.save(testBookingCancel);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName(value = "Тест на получение списка бронирований")
    void getAllTest_success() throws Exception {
        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName(value = "Тест на получение бронирования по id")
    void getBookingByIdTest_success() throws Exception {
        UUID id = testBooking.getId();

        mockMvc.perform(get("/bookings/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingDateTime").value(LocalDateTime.now().withNano(0).toString()))
                .andExpect(jsonPath("$.bookingStartDateTime").value(LocalDateTime.now().withNano(0).toString()))
                .andExpect(jsonPath("$.bookingEndDateTime").value(LocalDateTime.now().withNano(0).toString()))
                .andExpect(jsonPath("$.bookingCancelDateTime").value(LocalDateTime.now().withNano(0).toString()))
                .andExpect(jsonPath("$.cancelReason").value("Reason #1"))
                .andExpect(jsonPath("$.isBooked").value(false));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName(value = "Тест на получение бронирования по id")
    void getBookingByIdTest_failure() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000001212");

        mockMvc.perform(get("/bookings/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    @DisplayName(value = "Тест на создание бронирования")
    void createBookingTest_success() throws Exception {
        UUID userId = testUser.getId();
        UUID workplaceId = testWorkplace.getId();

        BookingCreateRequest bookingCreateRequest = new BookingCreateRequest(
                LocalDateTime.now().withNano(0),
                LocalDateTime.now().withNano(0),
                userId,
                workplaceId
        );

        String jsonCreateRequest = getMapper().writeValueAsString(bookingCreateRequest);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateRequest))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookingStartDateTime")
                        .value(LocalDateTime.now().withNano(0).toString()))
                .andExpect(jsonPath("$.bookingEndDateTime")
                        .value(LocalDateTime.now().withNano(0).toString()));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    @DisplayName(value = "Тест на отмену бронирования")
    void cancelBookingTest_success() throws Exception {
        UUID id = testBooking.getId();

        mockMvc.perform(get("/bookings/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cancelReason").value("Reason #1"))
                .andExpect(jsonPath("$.isBooked").value(false));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    @DisplayName(value = "Тест на отмену бронирования")
    void cancelBookingTest_failure() throws Exception {
        UUID id = testBookingCancel.getId();

        mockMvc.perform(get("/bookings/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cancelReason").value(""))
                .andExpect(jsonPath("$.isBooked").value(false));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    @DisplayName(value = "Тест на подтверждение бронирования")
    void confirmBookingTest_success() throws Exception {
        UUID id = testBooking.getId();

        mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/confirmation/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/bookings/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isBooked").value(true));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @WithMockUser(value = "admin", authorities = "ROLE_ADMIN")
    @DisplayName(value = "Тест на подтверждение бронирования")
    void confirmBookingTest_failure_NotFound() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000045");

        mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/confirmation/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/bookings/{id}", id))
                .andExpect(status().isNotFound());
    }
}
