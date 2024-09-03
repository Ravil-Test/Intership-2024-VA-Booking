package ru.irlix.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.irlix.booking.dto.office.OfficeCreateRequest;
import ru.irlix.booking.dto.office.OfficeResponse;
import ru.irlix.booking.dto.office.OfficeUpdateRequest;
import ru.irlix.booking.entity.Office;

import java.util.List;

/**
 * Маппер для офиса
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR, uses = RoomMapper.class)
public interface OfficeMapper {


    @Mapping(target = "isDelete", source = "delete")
    OfficeResponse entityToResponse(Office office);

    @Mapping(target = "delete", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "id", ignore = true)
    Office createRequestToEntity(OfficeCreateRequest createRequest);

    @Mapping(target = "delete", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "id", ignore = true)
    Office updateRequestToEntity(OfficeUpdateRequest updateRequest);

    List<OfficeResponse> entityListToResponseList(List<Office> offices);
}
