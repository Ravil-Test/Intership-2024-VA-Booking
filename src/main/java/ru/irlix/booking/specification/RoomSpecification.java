package ru.irlix.booking.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.irlix.booking.entity.Room;

import java.util.UUID;

/**
 * Фильтр помещений
 */
public class RoomSpecification {

    public static Specification<Room> hasName(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Room> isDeleted(Boolean isDelete) {
        return (root, query, cb) -> cb.equal(root.get("isDelete"), isDelete);
    }

    public static Specification<Room> hasFloorNumber(Short floorNumber) {
        return (root, query, cb) -> cb.equal(root.get("floorNumber"), floorNumber);
    }

    public static Specification<Room> hasRoomNumber(Short roomNumber) {
        return (root, query, cb) -> cb.equal(root.get("roomNumber"), roomNumber);
    }

    public static Specification<Room> hasOfficeId(UUID officeId) {
        return (root, query, cb) -> cb.equal(root.get("officeId"), officeId);
    }
}
