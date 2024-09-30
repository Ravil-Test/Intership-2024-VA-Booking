package ru.irlix.booking.specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.irlix.booking.entity.Booking;

import java.util.UUID;

/**
 * Фильтр бронирования
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingSpecifications {

    public static Specification<Booking> hasUserId(UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Booking> hasWorkplaceId(UUID workplaceId) {
        return (root, query, cb) -> cb.equal(root.get("workplace").get("id"), workplaceId);
    }

    public static Specification<Booking> isBooked(Boolean isBooked) {
        return (root, query, cb) -> cb.equal(root.get("isBooked"), isBooked);
    }
}