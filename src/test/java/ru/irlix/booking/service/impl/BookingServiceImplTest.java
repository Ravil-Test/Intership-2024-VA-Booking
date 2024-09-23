package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.irlix.booking.dto.booking.BookingCancelRequest;
import ru.irlix.booking.dto.booking.BookingCreateRequest;
import ru.irlix.booking.dto.booking.BookingResponse;
import ru.irlix.booking.dto.room.RoomCreateRequest;
import ru.irlix.booking.dto.room.RoomResponse;
import ru.irlix.booking.dto.room.RoomUpdateRequest;
import ru.irlix.booking.entity.Booking;
import ru.irlix.booking.entity.Room;
import ru.irlix.booking.repository.BookingRepository;
import ru.irlix.booking.service.BookingService;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName(value = "Тесты бизнес-логики офисов")
@TestPropertySource("classpath:application-test.properties")
@Sql({
        "classpath:sql/init_usersServiceImplTest.sql"
})
public class BookingServiceImplTest extends BaseIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;


    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение списка бронирований")
    void getBookingAllTest_success() {
        List<BookingResponse> ResponseList = List.of(
                new BookingResponse(LocalDateTime.now().withSecond(0).withNano(0),
                        LocalDateTime.now().withSecond(0).withNano(0),
                        LocalDateTime.now().withSecond(0).withNano(0),
                        LocalDateTime.now().withSecond(0).withNano(0),
                        "",
                        false));

        List<BookingResponse> currentResponseList = bookingService.getAll();

        assertEquals(ResponseList.size(), currentResponseList.size());
        assertFalse(currentResponseList.isEmpty());
        assertTrue(currentResponseList.stream().anyMatch(actualResponse -> actualResponse.equals(ResponseList.get(0))));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получения бронирования по id")
    void getByIdTest_BookingIsExist() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000003");

        BookingResponse currentResponse = bookingService.getById(id);

        assertNotNull(currentResponse);
        assertEquals(LocalDateTime.now().withSecond(0).withNano(0), currentResponse.bookingDateTime());
        assertEquals(LocalDateTime.now().withSecond(0).withNano(0), currentResponse.bookingStartDateTime());
        assertEquals(LocalDateTime.now().withSecond(0).withNano(0), currentResponse.bookingEndDateTime());
        assertEquals(LocalDateTime.now().withSecond(0).withNano(0), currentResponse.bookingCancelDateTime());
        assertEquals("", currentResponse.cancelReason());
        assertEquals(false, currentResponse.isBooked());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Тест на получения бронирования по id")
    void getByIdTest_BookingNotFound() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000323");
        assertThrows(EntityNotFoundException.class, () -> bookingService.getById(id));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на создание бронирования")
    void createBookingTest_success() {
        UUID userId = UUID.fromString("21212121-2121-2121-2121-212121212121");
        UUID workplaceId = UUID.fromString("55555555-5555-5555-5555-555555555555");

        BookingCreateRequest createRequest = new BookingCreateRequest(
                LocalDateTime.now().withSecond(0).withNano(0),
                LocalDateTime.now().withSecond(0).withNano(0),
                userId,
                workplaceId);

        BookingResponse savedBooking = bookingService.save(createRequest);

        assertNotNull(savedBooking);
        assertEquals(createRequest.bookingStartDateTime(), savedBooking.bookingStartDateTime());
        assertEquals(createRequest.bookingEndDateTime(), savedBooking.bookingEndDateTime());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест отмены бронирования")
    void cancelBookingTest_success() throws EntityNotFoundException {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000003");
        BookingCancelRequest cancelRequest = new BookingCancelRequest("");

        BookingResponse canceledBooking = bookingService.cancel(id, cancelRequest);

        assertNotNull(canceledBooking);
        assertEquals(cancelRequest.cancelReason(), canceledBooking.cancelReason());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на подтверждение бронирования")
    void confirmBookingTest_success() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000003");
        bookingService.confirm(id);

        Optional<Booking> confirmedBooking = bookingRepository.findById(id);
        assertTrue(confirmedBooking.isPresent());
        assertTrue(confirmedBooking.get().isBooked());
    }
}