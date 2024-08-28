package ru.irlix.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.irlix.booking.entity.Office;

import java.util.UUID;

/**
 * Репозиторий офиса
 */
public interface OfficeRepository extends JpaRepository<Office, UUID> {

    /**
     * Изменить статус офиса на удален/не удален
     *
     * @param id     -id офиса
     * @param delete - статус (удален - true/не удален - false)
     */
    @Modifying
    @Query("update Office o set o.isDelete = :delete where o.id = :id")
    void changeOfficeIsDelete(@Param("id") UUID id, @Param("delete") boolean delete);
}
