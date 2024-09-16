package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.irlix.booking.dto.role.RoleCreateRequest;
import ru.irlix.booking.dto.role.RoleResponse;
import ru.irlix.booking.dto.role.RoleUpdateRequest;
import ru.irlix.booking.entity.Role;
import ru.irlix.booking.mapper.RoleMapper;
import ru.irlix.booking.repository.RoleRepository;
import ru.irlix.booking.service.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse getById(UUID id) {
        Role role = getRoleWithNullCheck(id);
        log.info("Получение роли по id: {} : {}", id, role);
        return roleMapper.entityToResponse(role);
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }

    @Override
    public List<RoleResponse> getAll() {
        return roleMapper
                .entityListToResponseList(roleRepository.findAll());
    }

    @Override
    public RoleResponse save(@NonNull RoleCreateRequest createRequest) {
        Role createRole = roleMapper.createRequestToEntity(createRequest);

        Role saveRole = roleRepository.save(createRole);
        log.info("Создание роли {}", createRole);
        return roleMapper.entityToResponse(saveRole);
    }

    @Override
    public RoleResponse update(UUID id, @NonNull RoleUpdateRequest updateRequest) {
        Role currentRole = getRoleWithNullCheck(id);
        Role updateRole = roleMapper.updateRequestToEntity(updateRequest);

        Optional.ofNullable(updateRole.getName()).ifPresent(currentRole::setName);

        Role changedRole = roleRepository.save(currentRole);
        log.info("Обновлённая роль {}", changedRole);

        return roleMapper.entityToResponse(changedRole);
    }

    @Override
    public void delete(UUID id) {
        roleRepository.deleteById(id);
        log.info("Роль с id {} удалена", id);
    }

    /**
     * Получить роль с проверкой на null
     *
     * @param id - id роли
     * @return - найденная роль
     */
    private Role getRoleWithNullCheck(UUID id) {
        return roleRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(("Роль с заданным id - "
                                + id
                                + " не найдена")));
    }
}
