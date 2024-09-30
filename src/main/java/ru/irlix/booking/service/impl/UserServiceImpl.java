package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.dto.user.UserResponse;
import ru.irlix.booking.dto.user.UserSearchRequest;
import ru.irlix.booking.dto.user.UserUpdateRequest;
import ru.irlix.booking.entity.Role;
import ru.irlix.booking.entity.User;
import ru.irlix.booking.exception.RegistrationFailedException;
import ru.irlix.booking.mapper.UserMapper;
import ru.irlix.booking.repository.UserRepository;
import ru.irlix.booking.security.config.PasswordEncoder;
import ru.irlix.booking.service.RoleService;
import ru.irlix.booking.service.UserService;
import ru.irlix.booking.specification.UserSpecification;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ROLE = "USER";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(UUID id) {
        User user = getUserById(id);
        log.info("Получение пользователя по id: {} : {}", id, user);
        return userMapper.entityToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userMapper
                .entityListToResponseList(userRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> search(Pageable pageable, UserSearchRequest userRequest) {

        Specification<User> specification = Specification.where(null);

        if (userRequest != null && userRequest.fio() != null && !userRequest.fio().isEmpty()) {
            specification = specification.and(UserSpecification.hasFio(userRequest.fio()));
        }

        if (userRequest != null && userRequest.isDelete() != null) {
            specification = specification.and(UserSpecification.isDelete(userRequest.isDelete()));
        }

        Page<User> usersPage = userRepository.findAll(specification, pageable);

        log.info("Получение списка пользователей с пагинацией: {} записей на странице {}",
                usersPage.getTotalElements(), pageable.getPageSize());
        return usersPage.map(userMapper::entityToResponse);
    }


    @Override
    @Transactional
    public UserResponse save(@NonNull UserCreateRequest createRequest) {
        String actualPhoneNumber = createRequest.phoneNumber().replaceAll("\\D", "");

        if (userRepository.getUserByPhoneNumber(actualPhoneNumber).isPresent() || userRepository.getUserByEmail(createRequest.email()).isPresent())
            throw new RegistrationFailedException("Такой пользователь уже зарегистрирован");

        User createUser = userMapper.createRequestToEntity(createRequest);

        Role defaultRole = roleService.getByName(DEFAULT_ROLE);
        createUser.setRoles(Set.of(defaultRole));
        createUser.setPassword(passwordEncoder.passwordEncoder().encode(String.valueOf(createUser.getPassword())).toCharArray());
        User saveUser = userRepository.save(createUser);

        log.info("Создание пользователя {}", createUser);
        return userMapper.entityToResponse(saveUser);
    }

    @Override
    @Transactional
    public UserResponse update(UUID id, @NonNull UserUpdateRequest updateRequest) {
        User currentUser = getUserById(id);
        User updateUser = userMapper.updateRequestToEntity(updateRequest);

        Optional.ofNullable(updateUser.getFio()).ifPresent(currentUser::setFio);
        Optional.ofNullable(updateUser.getPhoneNumber()).ifPresent(currentUser::setPhoneNumber);
        Optional.ofNullable(updateUser.getEmail()).ifPresent(currentUser::setEmail);
        Optional.ofNullable(updateUser.getPassword()).ifPresent(currentUser::setPassword);

        User changedUser = userRepository.save(currentUser);
        log.info("Обновлённый пользователь {}", changedUser);

        return userMapper.entityToResponse(changedUser);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        userRepository.changeUserIsDeleted(id, true);
        log.info("Пользователь с id :{} удалён", id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;

        if (username.matches("^\\+?[0-9\\-\\s]*$")) {
            user = getUserByPhoneNumber(username);

            return new org.springframework.security.core.userdetails.User(
                    user.getPhoneNumber(),
                    String.valueOf(user.getPassword()),
                    user.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).toList()
            );
        } else if (username.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            user = getUserByEmail(username);

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    String.valueOf(user.getPassword()),
                    user.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).toList()
            );
        } else
            throw new IllegalArgumentException("Логин не соответствует номеру телефона или email адресу");
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(("User with id: " + id + " not found")));
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.getUserByPhoneNumber(phoneNumber).orElseThrow(()
                -> new EntityNotFoundException("User with phone number: " + phoneNumber + " not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(()
                -> new EntityNotFoundException("User with email: " + email + " not found"));
    }

}