package ru.irlix.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.irlix.booking.dto.booking.BookingCancelRequest;
import ru.irlix.booking.dto.booking.BookingCreateRequest;
import ru.irlix.booking.dto.booking.BookingResponse;
import ru.irlix.booking.entity.Booking;

import java.util.List;

/**
 * Маппер для бронирования
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BookingMapper {

    @Mapping(source = "booked", target = "isBooked")
    BookingResponse entityToResponse(Booking booking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookingDateTime", expression = "java(LocalDateTime.now())")
    @Mapping(target = "bookingCancelDateTime", ignore = true)
    @Mapping(target = "cancelReason", ignore = true)
    @Mapping(target = "booked", ignore = true)
    Booking createRequestToEntity(BookingCreateRequest createRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookingDateTime", ignore = true)
    @Mapping(target = "bookingStartDateTime", ignore = true)
    @Mapping(target = "bookingCancelDateTime", expression = "java(LocalDateTime.now())")
    @Mapping(target = "bookingEndDateTime", ignore = true)
    @Mapping(target = "booked", ignore = true)
    Booking cancelRequestToEntity(BookingCancelRequest cancelRequest);

    List<BookingResponse> entityListToResponseList(List<Booking> bookings);
}
