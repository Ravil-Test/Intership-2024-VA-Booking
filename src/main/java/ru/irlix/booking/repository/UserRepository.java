package ru.irlix.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.irlix.booking.entity.User;

import java.util.UUID;

/**
 * Репозиторий пользователей
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    /**
     * Изменить статус пользователя на удален/не удален
     *
     * @param id     -id пользователя
     * @param delete - статус (удален - true/не удален - false)
     */
    @Modifying
    @Query("update User u set u.isDelete = :delete where u.id = :id")
    void changeUserIsDeleted(@Param("id") UUID id, @Param("delete") boolean delete);
}