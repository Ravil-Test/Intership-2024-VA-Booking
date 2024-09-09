package ru.irlix.booking.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.irlix.booking.entity.Office;

/**
 * Фильтр офисов
 */
public class OfficeSpecifications {

    public static Specification<Office> hasName(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Office> isDeleted(Boolean isDelete) {
        return (root, query, cb) -> isDelete != null ? cb.equal(root.get("isDelete"), isDelete) : cb.conjunction();
    }
}
