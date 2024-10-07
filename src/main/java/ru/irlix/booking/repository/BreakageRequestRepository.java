package ru.irlix.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.irlix.booking.entity.BreakageRequest;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий заявок о поломках
 */
@Repository
public interface BreakageRequestRepository extends JpaRepository<BreakageRequest, UUID>,
        JpaSpecificationExecutor<BreakageRequest> {

    /**
     * Получить список заявок пользователя по его id
     *
     * @param userId - id пользователя
     * @return - список заявок пользователя
     */
    List<BreakageRequest> getBreakageRequestByUserId(UUID userId);

    /**
     * Изменение статуса isCanceled у заявки о поломке на true
     *
     * @param id     - id заявки
     * @param cancel - статус заявки (удален/не удален)
     */
    @Modifying
    @Query("update BreakageRequest b set b.isCanceled = :cancel where b.id = :id")
    void changeBreakageRequestIsCanceled(@Param("id") UUID id, @Param("cancel") boolean cancel);
}