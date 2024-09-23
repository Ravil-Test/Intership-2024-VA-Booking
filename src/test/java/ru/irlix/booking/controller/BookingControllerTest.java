package ru.irlix.booking.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.irlix.booking.dto.booking.BookingCreateRequest;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName(value = "Тесты контроллера бронирования>")
@TestPropertySource("classpath:application-test.properties")
@Sql({
        "classpath:sql/init_data.sql"
})
public class BookingControllerTest extends BaseIntegrationTest {

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение списка бронирований")
    void getAllTest_success() throws Exception {
        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение бронирования по id")
    void getBookingByIdTest_success() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        mockMvc.perform(get("/bookings/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingDateTime").value("2024-08-30T12:11:50.077721"))
                .andExpect(jsonPath("$.bookingStartDateTime").value("2024-08-30T12:11:50.077721"))
                .andExpect(jsonPath("$.bookingEndDateTime").value("2024-08-30T12:11:50.077721"))
                .andExpect(jsonPath("$.bookingCancelDateTime").value("2024-08-30T12:11:50.077721"))
                .andExpect(jsonPath("$.cancelReason").value("Reason #1"))
                .andExpect(jsonPath("$.isBooked").value(false));

    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на получение бронирования по id")
    void getBookingByIdTest_failure() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000001212");

        mockMvc.perform(get("/bookings/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на создание бронирования")
    void createBookingTest_success() throws Exception {
        UUID userId = UUID.fromString("21212121-2121-2121-2121-212121212121");
        UUID workplaceId = UUID.fromString("55555555-5555-5555-5555-555555555555");

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
    @DisplayName(value = "Тест на отмену бронирования")
    void cancelBookingTest_success() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        mockMvc.perform(get("/bookings/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cancelReason").value("Reason #1"))
                .andExpect(jsonPath("$.isBooked").value(false));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на отмену бронирования")
    void cancelBookingTest_failure() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000002");

        mockMvc.perform(get("/bookings/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cancelReason").value(""))
                .andExpect(jsonPath("$.isBooked").value(false));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на подтверждение бронирования")
    void confirmBookingTest_success() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/confirmation/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/bookings/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isBooked").value(true));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на подтверждение бронирования")
    void confirmBookingTest_failure() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000045");

        mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/confirmation/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/bookings/{id}", id))
                .andExpect(status().isNotFound());
    }
}
