package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestCreate;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestUpdate;
import ru.irlix.booking.dto.breakagerequest.BreakageResponse;
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
import ru.irlix.booking.service.BreakageRequestService;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName(value = "Тесты бизнес-логики заявок о поломках")
class BreakageRequestServiceImplTest extends BaseIntegrationTest {

    private BreakageRequest testBreakageRequest;

    private Workplace testWorkplace;

    @Autowired
    private BreakageRequestService breakageService;

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
        Workplace savedWorkplace = workplaceRepository.save(testWorkplace);

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
        User savedAdmin = userRepository.save(testAdmin);

        Booking testBookingAdmin = Booking.builder()
                .bookingDateTime(LocalDateTime.now())
                .bookingStartDateTime(LocalDateTime.now())
                .bookingEndDateTime(LocalDateTime.now().plusHours(1))
                .bookingCancelDateTime(null)
                .cancelReason(null)
                .isBooked(true)
                .workplace(savedWorkplace)
                .user(savedAdmin)
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
    @DisplayName(value = "Позитивный тест на получение заявки по id")
    void getById_success() {
        UUID id = testBreakageRequest.getId();

        BreakageResponse actualResponse = breakageService.getById(id);

        assertNotNull(actualResponse);
        assertEquals("Test breakage 1", actualResponse.description());
        assertFalse(actualResponse.isComplete());
        assertFalse(actualResponse.isCanceled());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Негативный тест на получение заявки по id")
    void getById_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> breakageService.getById(id));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @WithMockUser(username = "test.dev@gmail.com", authorities = "ROLE_ADMIN")
    @DisplayName(value = "Позитивный тест на создание заявки о поломке")
    void save_success() throws EntityNotFoundException {
        UUID workplaceId = testWorkplace.getId();

        BreakageRequestCreate createRequest = new BreakageRequestCreate("Hello Kitty", workplaceId);

        BreakageResponse savedResponse = breakageService.save(createRequest);

        assertNotNull(savedResponse);
        assertEquals(createRequest.description(), savedResponse.description());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @WithMockUser(username = "test.dev@gmail.com", authorities = "ROLE_ADMIN")
    @DisplayName(value = "Позитивный тест на создание заявки о поломке")
    void save_failed() throws EntityNotFoundException {
        BreakageRequestCreate createRequest = new BreakageRequestCreate("Hello Kitty", UUID.randomUUID());
        assertThrows(IllegalArgumentException.class, () -> breakageService.save(createRequest));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на обновление заявки о поломке")
    @Transactional(readOnly = true)
    void update_success() {
        UUID id = testBreakageRequest.getId();
        BreakageRequestUpdate updateRequest = new BreakageRequestUpdate("New description");

        BreakageResponse userResponse = breakageService.update(id, updateRequest);

        assertNotNull(userResponse);
        assertEquals(updateRequest.description(), userResponse.description());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Негативный тест на обновление заявки о поломке")
    void update_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        BreakageRequestUpdate updateRequest = new BreakageRequestUpdate("New description");

        assertThrows(EntityNotFoundException.class, () -> breakageService.update(id, updateRequest));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на удаление заявки о поломке")
    void delete_success() {
        UUID id = testBreakageRequest.getId();
        breakageService.delete(id);

        BreakageResponse foundRequest = breakageService.getById(id);
        assertNotNull(foundRequest);
        assertTrue(foundRequest.isCanceled());
    }
}