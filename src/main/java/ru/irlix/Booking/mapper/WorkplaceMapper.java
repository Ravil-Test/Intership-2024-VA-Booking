package ru.irlix.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.irlix.booking.dto.workplace.WorkplaceCreateRequest;
import ru.irlix.booking.dto.workplace.WorkplaceResponse;
import ru.irlix.booking.dto.workplace.WorkplaceUpdateRequest;
import ru.irlix.booking.entity.Workplace;

import java.util.List;

/**
 * Маппер для рабочего места
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface WorkplaceMapper {

    @Mapping(target = "isDelete", ignore = true)
    WorkplaceResponse entityToResponse(Workplace workplace);

    List<WorkplaceResponse> entityListToReponseList(List<Workplace> workplaces);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "delete", ignore = true)
    Workplace createRequestToEntity(WorkplaceCreateRequest createRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "delete", ignore = true)
    Workplace updateRequestToEntity(WorkplaceUpdateRequest updateRequest);
}
