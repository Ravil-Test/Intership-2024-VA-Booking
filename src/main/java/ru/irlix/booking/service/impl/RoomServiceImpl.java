package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.irlix.booking.dto.room.RoomCreateRequest;
import ru.irlix.booking.dto.room.RoomResponse;
import ru.irlix.booking.dto.room.RoomUpdateRequest;
import ru.irlix.booking.entity.Room;
import ru.irlix.booking.mapper.RoomMapper;
import ru.irlix.booking.repository.RoomRepository;
import ru.irlix.booking.service.RoomService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public RoomResponse getById(UUID id) {
        Room room = getRoomWithNullCheck(id);
        log.info("Получение помещения по id: {} : {}", id, room);
        return roomMapper.entityToResponse(room);
    }

    @Override
    public List<RoomResponse> getAll() {
        return roomMapper
                .entityListToResponseList(roomRepository.findAll());
    }

    @Override
    public RoomResponse save(@NonNull RoomCreateRequest createRequest) {
        Room createRoom = roomMapper.createRequestToEntity(createRequest);

        Room saveRoom = roomRepository.save(createRoom);
        log.info("Создание помещения {}", createRoom);
        return roomMapper.entityToResponse(saveRoom);
    }

    @Override
    public RoomResponse update(UUID id, @NonNull RoomUpdateRequest updateRequest) {
        Room currentRoom = getRoomWithNullCheck(id);
        Room updateRoom = roomMapper.updateRequestToEntity(updateRequest);

        Optional.ofNullable(updateRoom.getName()).ifPresent(currentRoom::setName);
        Optional.ofNullable(updateRoom.getFloorNumber()).ifPresent(currentRoom::setFloorNumber);
        Optional.ofNullable(updateRoom.getRoomNumber()).ifPresent(currentRoom::setRoomNumber);

        Room changedRoom = roomRepository.save(currentRoom);
        log.info("Обновлённое помещение {}", changedRoom);

        return roomMapper.entityToResponse(changedRoom);
    }

    @Override
    public void delete(UUID id) {
        roomRepository.changeRoomIsDeleted(id, true);
        log.info("Помещение с id {} удалено", id);
    }

    /**
     * Получить помещение с проверкой на null
     *
     * @param id - id помещения
     * @return - найденное помещение
     */
    private Room getRoomWithNullCheck(UUID id) {
        return roomRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(("Помещение с заданным id - "
                                + id
                                + " не найдено")));
    }
}
