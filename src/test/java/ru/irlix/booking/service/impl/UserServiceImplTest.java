package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.irlix.booking.dto.role.RoleResponse;
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.dto.user.UserResponse;
import ru.irlix.booking.dto.user.UserUpdateRequest;
import ru.irlix.booking.entity.Role;
import ru.irlix.booking.entity.User;
import ru.irlix.booking.repository.RoleRepository;
import ru.irlix.booking.repository.UserRepository;
import ru.irlix.booking.service.RoleService;
import ru.irlix.booking.service.UserService;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName(value = "Тесты безнес-логики пользователей")
class UserServiceImplTest extends BaseIntegrationTest {

    private User testUser;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

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
                .availableMinutesForBooking(30)
                .isDelete(false)
                .build();
        userRepository.save(testUser);

        User testUser2 = User.builder()
                .fio("Petrov Ignat Petrovich")
                .phoneNumber("88004000500")
                .email("petr@gmail.com")
                .roles(Set.of(savedRole))
                .password(new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'})
                .availableMinutesForBooking(20)
                .isDelete(true)
                .build();
        userRepository.save(testUser2);

        User testUser3 = User.builder()
                .fio("Sidorov Sergey Ivanovich")
                .phoneNumber("88005000600")
                .email("sidr@gmail.com")
                .roles(Set.of(savedRole))
                .password(new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'})
                .availableMinutesForBooking(120)
                .isDelete(false)
                .build();
        userRepository.save(testUser3);
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на получение пользователя по id")
    void getById_success() {
        UUID id = testUser.getId();

        UserResponse actualResponse = userService.getById(id);

        assertNotNull(actualResponse);
        assertEquals("Ignatiev Ignat Ignatievich", actualResponse.fio());
        assertEquals("88003000400", actualResponse.phoneNumber());
        assertEquals("ignat@yandex.ru", actualResponse.email());
        assertEquals(30, actualResponse.availableMinutesForBooking());
        assertFalse(actualResponse.isDelete());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Негативный тест на получение пользователя по id")
    void getById_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> userService.getById(id));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на получение списка пользователей")
    void getAll_success() {
        RoleResponse roleUser = new RoleResponse("USER");

        List<UserResponse> expectedResponseList = List.of(
                new UserResponse("Ignatiev Ignat Ignatievich", "88003000400",
                        "ignat@yandex.ru", 30, false, Set.of(roleUser)),
                new UserResponse("Petrov Ignat Petrovich", "88004000500",
                        "petr@gmail.com", 20, true, Set.of(roleUser)),
                new UserResponse("Sidorov Sergey Ivanovich", "88005000600",
                        "sidr@gmail.com", 120, false, Set.of(roleUser)));

        List<UserResponse> actualResponseList = userService.getAll();

        assertEquals(expectedResponseList.size(), actualResponseList.size());
        assertFalse(actualResponseList.isEmpty());
        assertTrue(actualResponseList.stream().anyMatch(actualResponse -> actualResponse.equals(expectedResponseList.get(0))));
        assertTrue(actualResponseList.stream().anyMatch(actualResponse -> actualResponse.equals(expectedResponseList.get(1))));
        assertTrue(actualResponseList.stream().anyMatch(actualResponse -> actualResponse.equals(expectedResponseList.get(2))));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на создание пользователя")
    void save_success() {
        UserCreateRequest createRequest = new UserCreateRequest("Petrova Marina Petrovna",
                "79142480689", "petrova@rambler.ru", "password123".toCharArray());

        Role defaultRole = roleService.getByName("USER");
        UserResponse savedResponse = userService.save(createRequest);

        assertNotNull(defaultRole, "Роль по умолчанию не найдена");
        assertNotNull(savedResponse);
        assertEquals(createRequest.fio(), savedResponse.fio());
        assertEquals(createRequest.phoneNumber(), savedResponse.phoneNumber());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на обновление пользователя")
    void update_success() {
        UUID userId = testUser.getId();
        UserUpdateRequest updateRequest = new UserUpdateRequest("Petrova Marina Petrovna",
                "79142480689", "petrova@rambler.ru", "password123".toCharArray());

        UserResponse updatedUser = userService.update(userId, updateRequest);

        assertNotNull(updatedUser);
        assertEquals(updateRequest.fio(), updatedUser.fio());
        assertEquals(updateRequest.phoneNumber(), updatedUser.phoneNumber());
        assertEquals(updateRequest.email(), updatedUser.email());
    }

    @Test
    @DirtiesContext
    @Tag(value = "Негативный")
    @DisplayName(value = "Негативный тест на обновление пользователя")
    void update_notFound() throws EntityNotFoundException {
        UUID id = UUID.randomUUID();
        UserUpdateRequest updateRequest = new UserUpdateRequest("Petrova Marina Petrovna",
                "79142480689", "petrova@rambler.ru", "password123".toCharArray());

        assertThrows(EntityNotFoundException.class, () -> userService.update(id, updateRequest));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на удаление пользователя")
    void delete_success() {
        UUID userId = testUser.getId();
        userService.delete(userId);

        Optional<User> deletedRoom = userRepository.findById(userId);
        assertTrue(deletedRoom.isPresent());
        assertTrue(deletedRoom.get().getIsDelete());
    }
}