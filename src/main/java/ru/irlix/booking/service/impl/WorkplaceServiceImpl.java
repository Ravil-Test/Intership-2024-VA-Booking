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
import ru.irlix.booking.dto.workplace.WorkplaceCreateRequest;
import ru.irlix.booking.dto.workplace.WorkplaceResponse;
import ru.irlix.booking.dto.workplace.WorkplaceSearchRequest;
import ru.irlix.booking.dto.workplace.WorkplaceUpdateRequest;
import ru.irlix.booking.entity.Workplace;
import ru.irlix.booking.mapper.WorkplaceMapper;
import ru.irlix.booking.repository.WorkplaceRepository;
import ru.irlix.booking.service.RoomService;
import ru.irlix.booking.service.WorkplaceService;
import ru.irlix.booking.specification.WorkplaceSpecification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkplaceServiceImpl implements WorkplaceService {

    private final WorkplaceRepository workplaceRepository;
    private final WorkplaceMapper workplaceMapper;
    private final RoomService roomService;

    @Override
    public WorkplaceResponse getById(UUID id) {
        Workplace foundWorkplace = optionalCheck(id);
        log.info("Get workplace with id: {}: {}", id, foundWorkplace);

        return workplaceMapper.entityToResponse(foundWorkplace);
    }

    @Override
    public List<WorkplaceResponse> getAll() {
        return workplaceMapper.entityListToReponseList(workplaceRepository.findAll());
    }

    @Override
    public Page<WorkplaceResponse> search(WorkplaceSearchRequest searchRequest, Pageable pageable) {
        Specification<Workplace> spec = Specification.where(null);

        if (searchRequest.number() != null) {
            spec = spec.and(WorkplaceSpecification.hasNumber(searchRequest.number()));
        }
        if (searchRequest.isDelete() != null) {
            spec = spec.and(WorkplaceSpecification.isDeleted(searchRequest.isDelete()));
        }
        if (searchRequest.roomId() != null) {
            spec = spec.and(WorkplaceSpecification.hasRoomId(searchRequest.roomId()));
        }

        Page<Workplace> workplacePage = workplaceRepository.findAll(spec, pageable);
        return workplacePage.map(workplaceMapper::entityToResponse);
    }

    @Override
    public WorkplaceResponse save(@NonNull WorkplaceCreateRequest createRequest) {
        Workplace forSave = workplaceMapper.createRequestToEntity(createRequest);

        forSave.setRoom(roomService.getRoomWithNullCheck(createRequest.roomId()));

        Workplace saved = workplaceRepository.save(forSave);
        log.info("Save workplace: {} ", saved);

        return workplaceMapper.entityToResponse(saved);
    }

    @Override
    public WorkplaceResponse update(UUID id, @NonNull WorkplaceUpdateRequest updateRequest) {
        Workplace currentWorkplace = optionalCheck(id);
        Workplace update = workplaceMapper.updateRequestToEntity(updateRequest);

        if (Optional.ofNullable(updateRequest.roomId()).isPresent())
            update.setRoom(roomService.getRoomWithNullCheck(updateRequest.roomId()));
        Optional.ofNullable(update.getNumber()).ifPresent(currentWorkplace::setNumber);
        Optional.ofNullable(update.getDescription()).ifPresent(currentWorkplace::setDescription);

        Workplace changed = workplaceRepository.save(currentWorkplace);
        log.info("Update workplace with id: {} : {}", id, changed);

        return workplaceMapper.entityToResponse(changed);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        workplaceRepository.changeWorkplaceIsDelete(id, true);
        log.info("Delete workplace with id: {}", id);
    }

    /**
     * Получить рабочее место по id с проверкой на null
     *
     * @param id - id рабочего места
     * @return - найденное рабочее место
     */
    protected Workplace optionalCheck(UUID id) {
        return workplaceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workplace with id " + id + " not found"));
    }
}
