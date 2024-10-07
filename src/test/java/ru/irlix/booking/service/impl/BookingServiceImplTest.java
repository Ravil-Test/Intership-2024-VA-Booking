package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.irlix.booking.dto.booking.BookingCancelRequest;
import ru.irlix.booking.dto.booking.BookingCreateRequest;
import ru.irlix.booking.dto.booking.BookingResponse;
import ru.irlix.booking.dto.user.UserBookingResponse;
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
class BookingServiceImplTest extends BaseIntegrationTest {

    private Booking testBooking;

    private User testUser;

    private Workplace testWorkplace;

    @Autowired
    private BookingService bookingService;

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
                .password("password123".toCharArray())
                .availableMinutesForBooking(120)
                .isDelete(false)
                .build();
        User savedUser = userRepository.save(testUser);

        testBooking = Booking.builder()
                .bookingDateTime(LocalDateTime.now().withNano(0))
                .bookingStartDateTime(LocalDateTime.now().withNano(0))
                .bookingEndDateTime(LocalDateTime.now().withNano(0))
                .bookingCancelDateTime(LocalDateTime.now().withNano(0))
                .cancelReason("")
                .isBooked(false)
                .user(savedUser)
                .workplace(savedWorkplace)
                .build();
        bookingRepository.save(testBooking);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Тест на получение списка бронирований")
    void getBookingAllTest_success() {
        UserBookingResponse userBookingResponse = new UserBookingResponse("Sidorov Ivan Ivanovich",
                "88002000600", "sidorov.dev@gmail.com");

        List<BookingResponse> ResponseList = List.of(
                new BookingResponse(LocalDateTime.now().withNano(0),
                        LocalDateTime.now().withNano(0),
                        LocalDateTime.now().withNano(0),
                        LocalDateTime.now().withNano(0),
                        "",
                        false, userBookingResponse));

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
        UUID id = testBooking.getId();

        BookingResponse currentResponse = bookingService.getById(id);

        assertNotNull(currentResponse);
        assertEquals(LocalDateTime.now().withNano(0), currentResponse.bookingDateTime());
        assertEquals(LocalDateTime.now().withNano(0), currentResponse.bookingStartDateTime());
        assertEquals(LocalDateTime.now().withNano(0), currentResponse.bookingEndDateTime());
        assertEquals(LocalDateTime.now().withNano(0), currentResponse.bookingCancelDateTime());
        assertEquals("", currentResponse.cancelReason());
        assertFalse(currentResponse.isBooked());
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
        UUID userId = testUser.getId();
        UUID workplaceId = testWorkplace.getId();

        BookingCreateRequest createRequest = new BookingCreateRequest(
                LocalDateTime.now().plusHours(1).withSecond(0).withNano(0),
                LocalDateTime.now().plusHours(2).withSecond(0).withNano(0),
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
        UUID id = testBooking.getId();
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
        UUID id = testBooking.getId();
        bookingService.confirm(id);

        Optional<Booking> confirmedBooking = bookingRepository.findById(id);
        assertTrue(confirmedBooking.isPresent());
        assertTrue(confirmedBooking.get().isBooked());
    }
}