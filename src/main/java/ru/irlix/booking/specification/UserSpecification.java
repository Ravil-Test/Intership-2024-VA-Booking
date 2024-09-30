package ru.irlix.booking.specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.irlix.booking.entity.User;

/**
 * Фильтр пользователей
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpecification {

    public static Specification<User> hasFio(String fio) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("fio")), "%" + fio.toLowerCase() + "%");
    }

    public static Specification<User> isDelete(Boolean isDelete) {
        return (root, query, cb) ->
                cb.equal(root.get("isDelete"), isDelete);
    }
}