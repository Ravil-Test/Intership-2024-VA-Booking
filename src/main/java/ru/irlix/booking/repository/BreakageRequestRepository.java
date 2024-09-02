package ru.irlix.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.booking.entity.BreakageRequest;

import java.util.UUID;

/**
 * Репозиторий заявок о поломках
 */
public interface BreakageRequestRepository extends JpaRepository<BreakageRequest, UUID> {
}