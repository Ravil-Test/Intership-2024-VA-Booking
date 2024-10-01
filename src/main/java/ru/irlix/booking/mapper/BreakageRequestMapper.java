package ru.irlix.booking.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestCreate;
import ru.irlix.booking.dto.breakagerequest.BreakageRequestUpdate;
import ru.irlix.booking.dto.breakagerequest.BreakageResponse;
import ru.irlix.booking.entity.BreakageRequest;

import java.util.List;

/**
 * Маппер для заявок о поломках
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = WorkplaceMapper.class,
        builder = @Builder(disableBuilder = true))
public interface BreakageRequestMapper {

    @Mapping(target = "isComplete", ignore = true)
    @Mapping(target = "isCanceled", ignore = true)
    BreakageResponse entityToResponse(BreakageRequest breakageRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "complete", ignore = true)
    @Mapping(target = "canceled", ignore = true)
    @Mapping(target = "requestDateTime", expression = "java(LocalDateTime.now())")
    @Mapping(target = "workplace", ignore = true)
    @Mapping(target = "user", ignore = true)
    BreakageRequest createRequestToEntity(BreakageRequestCreate createBreakageRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "complete", ignore = true)
    @Mapping(target = "canceled", ignore = true)
    @Mapping(target = "requestDateTime", expression = "java(LocalDateTime.now())")
    @Mapping(target = "workplace", ignore = true)
    @Mapping(target = "user", ignore = true)
    BreakageRequest updateRequestToEntity(BreakageRequestUpdate updateBreakageRequest);

    List<BreakageResponse> entityToResponse(List<BreakageRequest> breakageRequests);
}
