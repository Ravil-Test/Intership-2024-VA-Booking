package ru.irlix.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.irlix.booking.entity.Room;

import java.util.UUID;

/**
 * Репозиторий помещения
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, UUID>, JpaSpecificationExecutor<Room> {

    /**
     * Изменить статус помещения на удален/не удален
     *
     * @param id     -id помещения
     * @param delete - статус (удален - true/не удален - false)
     */
    @Modifying
    @Query("update Room r set r.isDelete = :delete where r.id = :id")
    void changeRoomIsDeleted(@Param("id") UUID id, @Param("delete") boolean delete);
}