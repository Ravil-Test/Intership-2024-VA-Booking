package ru.irlix.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.irlix.booking.entity.Booking;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>, JpaSpecificationExecutor<Booking> {

    /**
     * Изменить статус бронирования на подтвержден/не подтвержден
     *
     * @param id        -id бронирования
     * @param confirmed - статус (подтвержден - true/не подтвержден - false)
     */
    @Modifying
    @Query("update Booking o set o.isBooked = :is_booked where o.id = :id")
    void changeBookingToConfirmed(@Param("id") UUID id, @Param("is_booked") boolean confirmed);

    /**
     * Проверка на доступность рабочего места перед бронированием, на заданный интервал времени
     *
     * @param workplaceId - id рабочего места
     * @param startTime   - начало бронирования
     * @param endTime     - завершение бронирования
     * @return - результат проверки (true / false)
     */
    @Query("select count(b) > 0 FROM Booking b where b.workplace.id = :workplaceId and"
            + " (:startTime between b.bookingStartDateTime and b.bookingEndDateTime or"
            + " :endTime between b.bookingStartDateTime and b.bookingEndDateTime or"
            + " b.bookingStartDateTime between :startTime and :endTime)")
    boolean checkAvailabilityWorkplace(@Param("workplaceId") UUID workplaceId,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);
}
