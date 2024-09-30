package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.irlix.booking.dto.room.RoomCreateRequest;
import ru.irlix.booking.dto.room.RoomResponse;
import ru.irlix.booking.dto.room.RoomSearchRequest;
import ru.irlix.booking.dto.room.RoomUpdateRequest;
import ru.irlix.booking.entity.Room;
import ru.irlix.booking.mapper.RoomMapper;
import ru.irlix.booking.repository.RoomRepository;
import ru.irlix.booking.service.OfficeService;
import ru.irlix.booking.service.RoomService;
import ru.irlix.booking.specification.RoomSpecification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final OfficeService officeService;

    @Override
    public RoomResponse getById(UUID id) {
        Room room = getRoomById(id);
        log.info("Получение помещения по id: {} : {}", id, room);
        return roomMapper.entityToResponse(room);
    }

    @Override
    public List<RoomResponse> getAll() {
        return roomMapper
                .entityListToResponseList(roomRepository.findAll());
    }

    @Override
    public Page<RoomResponse> search(RoomSearchRequest searchRequest, Pageable pageable) {
        Specification<Room> spec = Specification.where(null);

        if (StringUtils.hasText(searchRequest.name())) {
            spec = spec.and(RoomSpecification.hasName(searchRequest.name()));
        }
        if (searchRequest.isDelete() != null) {
            spec = spec.and(RoomSpecification.isDeleted(searchRequest.isDelete()));
        }
        if (searchRequest.floorNumber() != null) {
            spec = spec.and(RoomSpecification.hasFloorNumber(searchRequest.floorNumber()));
        }
        if (searchRequest.roomNumber() != null) {
            spec = spec.and(RoomSpecification.hasRoomNumber(searchRequest.roomNumber()));
        }
        if (searchRequest.officeId() != null) {
            spec = spec.and(RoomSpecification.hasOfficeId(searchRequest.officeId()));
        }

        Page<Room> roomPage = roomRepository.findAll(spec, pageable);
        if (!roomPage.hasContent())
            throw new EntityNotFoundException("Записей с таким фильтром не найдено");

        return roomPage.map(roomMapper::entityToResponse);
    }

    @Override
    @Transactional
    public RoomResponse save(@NonNull RoomCreateRequest createRequest) {
        Room createRoom = roomMapper.createRequestToEntity(createRequest);

        createRoom.setOffice(officeService.getOfficeById(createRequest.officeId()));

        Room saveRoom = roomRepository.save(createRoom);
        log.info("Создание помещения {}", createRoom);
        return roomMapper.entityToResponse(saveRoom);
    }

    @Override
    @Transactional
    public RoomResponse update(UUID id, @NonNull RoomUpdateRequest updateRequest) {
        Room currentRoom = getRoomById(id);
        Room updateRoom = roomMapper.updateRequestToEntity(updateRequest);

        if (Optional.ofNullable(updateRequest.officeId()).isPresent())
            updateRoom.setOffice(officeService.getOfficeById(updateRequest.officeId()));
        Optional.ofNullable(updateRoom.getName()).ifPresent(currentRoom::setName);
        Optional.ofNullable(updateRoom.getFloorNumber()).ifPresent(currentRoom::setFloorNumber);
        Optional.ofNullable(updateRoom.getRoomNumber()).ifPresent(currentRoom::setRoomNumber);

        Room changedRoom = roomRepository.save(currentRoom);
        log.info("Обновлённое помещение {}", changedRoom);

        return roomMapper.entityToResponse(changedRoom);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (getRoomById(id) != null)
            roomRepository.changeRoomIsDeleted(id, true);
        log.info("Помещение с id {} удалено", id);
    }

    @Override
    public Room getRoomById(UUID id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Помещение с заданным id - " + id + " не найдено"));
    }
}
