package ru.irlix.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.irlix.booking.entity.Booking;

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
}
