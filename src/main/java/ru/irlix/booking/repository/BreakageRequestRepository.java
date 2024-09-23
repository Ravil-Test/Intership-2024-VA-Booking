package ru.irlix.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.irlix.booking.entity.BreakageRequest;

import java.util.UUID;

/**
 * Репозиторий заявок о поломках
 */
@Repository
public interface BreakageRequestRepository extends JpaRepository<BreakageRequest, UUID> {
}