package ru.irlix.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.irlix.booking.dto.room.RoomCreateRequest;
import ru.irlix.booking.dto.room.RoomResponse;
import ru.irlix.booking.dto.room.RoomUpdateRequest;
import ru.irlix.booking.entity.Room;

import java.util.List;

/**
 * Маппер для помещения
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RoomMapper {

    @Mapping(target = "isDelete", ignore = true)
    RoomResponse entityToResponse(Room room);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "delete", ignore = true)
    Room createRequestToEntity(RoomCreateRequest createRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "delete", ignore = true)
    Room updateRequestToEntity(RoomUpdateRequest updateRequest);

    List<RoomResponse> entityListToResponseList(List<Room> rooms);
}
