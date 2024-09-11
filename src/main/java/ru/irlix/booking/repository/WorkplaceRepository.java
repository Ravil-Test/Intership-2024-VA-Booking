package ru.irlix.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.irlix.booking.entity.Workplace;

import java.util.UUID;

/**
 * Репозиторий рабочего места
 */
@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, UUID>, JpaSpecificationExecutor<Workplace> {

    /**
     * Изменить статус рабочего места на удален
     *
     * @param id     - id рабочего места
     * @param delete - статус (удален - true/не удален - false)
     */
    @Modifying
    @Query("update Workplace w set w.isDelete = :delete where w.id = :id")
    void changeWorkplaceIsDelete(@Param("id") UUID id, @Param("delete") boolean delete);
}
