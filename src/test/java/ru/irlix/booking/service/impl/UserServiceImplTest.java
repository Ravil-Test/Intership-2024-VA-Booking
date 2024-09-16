package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.dto.user.UserResponse;
import ru.irlix.booking.dto.user.UserUpdateRequest;
import ru.irlix.booking.entity.Role;
import ru.irlix.booking.entity.User;
import ru.irlix.booking.repository.UserRepository;
import ru.irlix.booking.service.RoleService;
import ru.irlix.booking.service.UserService;
import ru.irlix.booking.util.BaseIntegrationTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "Тесты безнес-логики пользователей")
@TestPropertySource("classpath:application-test.properties")
@Sql({
        "classpath:sql/init_usersServiceImplTest.sql"
})
class UserServiceImplTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на получение пользователя по id")
    void getById_success() {
        UUID id = UUID.fromString("21212121-2121-2121-2121-212121212121");

        UserResponse actualResponse = userService.getById(id);

        assertNotNull(actualResponse);
        assertEquals("Sidorov Ivan Ivanovich", actualResponse.fio());
        assertEquals("88002000600", actualResponse.phoneNumber());
        assertEquals("sidorov.dev@gmail.com", actualResponse.email());
        assertEquals(120, actualResponse.availableMinutesForBooking());
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
        UUID id = UUID.fromString("15151515-1515-1515-1515-151515151515");
        Role roleUser = new Role(id, "USER");

        List<UserResponse> expectedResponseList = List.of(
                new UserResponse("Sidorov Ivan Ivanovich", "88002000600",
                        "sidorov.dev@gmail.com", 120, false, Set.of(roleUser)),
                new UserResponse("Ignatiev Ignat Ignatievich", "88003000400",
                        "ignat@yandex.ru", 30, false, Set.of(roleUser)));

        List<UserResponse> actualResponseList = userService.getAll();

        assertEquals(expectedResponseList.size(), actualResponseList.size());
        assertFalse(actualResponseList.isEmpty());
        assertTrue(actualResponseList.stream().anyMatch(actualResponse -> actualResponse.equals(expectedResponseList.get(0))));
        assertTrue(actualResponseList.stream().anyMatch(actualResponse -> actualResponse.equals(expectedResponseList.get(1))));
    }

    @Test
    @DirtiesContext
    @Tag(value = "Позитивный")
    @DisplayName(value = "Позитивный тест на создание пользователя")
    void save_success() {
        UserCreateRequest createRequest = new UserCreateRequest("Petrova Marina Petrovna",
                "79142480689", "petrova@rambler.ru", "password123".toCharArray());

        Role defaultRole = roleService.getRoleByName("USER");
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
        UUID userId = UUID.fromString("21212121-2121-2121-2121-212121212121");
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
        UUID userId = UUID.fromString("21212121-2121-2121-2121-212121212121");
        userService.delete(userId);

        Optional<User> deletedRoom = userRepository.findById(userId);
        assertTrue(deletedRoom.isPresent());
        assertTrue(deletedRoom.get().isDelete());
    }
}