package ru.irlix.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.irlix.booking.dto.role.RoleCreateRequest;
import ru.irlix.booking.dto.role.RoleResponse;
import ru.irlix.booking.dto.role.RoleUpdateRequest;
import ru.irlix.booking.entity.Role;

import java.util.List;

/**
 * Маппер для роли
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RoleMapper {


    RoleResponse entityToResponse(Role role);

    @Mapping(target = "id", ignore = true)
    Role createRequestToEntity(RoleCreateRequest createRequest);

    @Mapping(target = "id", ignore = true)
    Role updateRequestToEntity(RoleUpdateRequest updateRequest);

    List<RoleResponse> entityListToResponseList(List<Role> roles);
}
