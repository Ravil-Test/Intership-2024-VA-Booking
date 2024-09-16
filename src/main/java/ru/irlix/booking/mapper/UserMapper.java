package ru.irlix.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.irlix.booking.dto.user.UserCreateRequest;
import ru.irlix.booking.dto.user.UserResponse;
import ru.irlix.booking.dto.user.UserUpdateRequest;
import ru.irlix.booking.entity.User;

import java.util.List;

/**
 * Маппер для пользователей
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {BookingMapper.class, BreakageRequestMapper.class})
public interface UserMapper {

    @Mapping(target = "isDelete", source = "delete")
    UserResponse entityToResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "delete", ignore = true)
    @Mapping(target = "availableMinutesForBooking", constant = "30")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "breakageRequests", ignore = true)
    User createRequestToEntity(UserCreateRequest createRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "delete", ignore = true)
    @Mapping(target = "availableMinutesForBooking", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "breakageRequests", ignore = true)
    User updateRequestToEntity(UserUpdateRequest updateRequest);

    List<UserResponse> entityListToResponseList(List<User> users);
}
