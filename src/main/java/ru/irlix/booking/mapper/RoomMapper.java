package ru.irlix.booking.mapper;

import org.mapstruct.Builder;
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
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = OfficeMapper.class,
        builder = @Builder(disableBuilder = true))
public interface RoomMapper {

    @Mapping(target = "isDelete", source = "delete")
    RoomResponse entityToResponse(Room room);

    @Mapping(target = "delete", ignore = true)
    @Mapping(target = "workplaces", ignore = true)
    @Mapping(target = "office", ignore = true)
    @Mapping(target = "id", ignore = true)
    Room createRequestToEntity(RoomCreateRequest createRequest);

    @Mapping(target = "delete", ignore = true)
    @Mapping(target = "workplaces", ignore = true)
    @Mapping(target = "office", ignore = true)
    @Mapping(target = "id", ignore = true)
    Room updateRequestToEntity(RoomUpdateRequest updateRequest);

    List<RoomResponse> entityListToResponseList(List<Room> rooms);
}
