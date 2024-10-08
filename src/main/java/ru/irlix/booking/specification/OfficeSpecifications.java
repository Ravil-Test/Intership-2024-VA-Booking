package ru.irlix.booking.specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.irlix.booking.entity.Office;

/**
 * Фильтр офисов
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OfficeSpecifications {

    public static Specification<Office> hasName(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Office> isDeleted(Boolean isDelete) {
        return (root, query, cb) -> cb.equal(root.get("isDelete"), isDelete);
    }

    public static Specification<Office> hasAddress(String address) {
        return (root, query, cb) -> cb.equal(root.get("address"), address);
    }
}
