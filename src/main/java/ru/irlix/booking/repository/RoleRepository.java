package ru.irlix.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.irlix.booking.entity.Role;

import java.util.UUID;

/**
 * Репозиторий роли
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role findRoleByName(String name);
}