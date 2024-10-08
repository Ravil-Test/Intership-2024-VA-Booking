package ru.irlix.booking.specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.irlix.booking.entity.Workplace;

import java.util.UUID;

/**
 * Фильтр рабочих мест
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkplaceSpecification {

    public static Specification<Workplace> isDeleted(Boolean isDelete) {
        return (root, query, cb) -> cb.equal(root.get("isDelete"), isDelete);
    }

    public static Specification<Workplace> hasNumber(Integer number) {
        return (root, query, cb) -> cb.equal(root.get("number"), number);
    }

    public static Specification<Workplace> hasRoomId(UUID roomId) {
        return (root, query, cb) -> cb.equal(root.get("roomId"), roomId);
    }
}
