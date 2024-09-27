package ru.irlix.booking.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.irlix.booking.entity.BreakageRequest;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Фильтр заявок о поломке
 */
public class BreakageSpecification {

    public static Specification<BreakageRequest> hasRequestDateTime(LocalDateTime requestDateTime) {
        return (root, query, cb) ->
                cb.equal(root.get("requestDateTime"), requestDateTime);
    }

    public static Specification<BreakageRequest> hasDescription(String description) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<BreakageRequest> hasWorkplace(UUID workplaceId) {
        return (root, query, cb) ->
                cb.equal(root.get("workplace").get("id"), workplaceId);
    }

    public static Specification<BreakageRequest> hasUser(UUID userId) {
        return (root, query, cb) ->
                cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<BreakageRequest> isComplete(Boolean isComplete) {
        return (root, query, cb) ->
                cb.equal(root.get("isComplete"), isComplete);
    }

    public static Specification<BreakageRequest> isCanceled(Boolean isCanceled) {
        return (root, query, cb) ->
                cb.equal(root.get("isCanceled"), isCanceled);
    }


}