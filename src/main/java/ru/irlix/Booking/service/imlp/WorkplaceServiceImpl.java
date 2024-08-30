package ru.irlix.booking.service.imlp;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.irlix.booking.dto.workplace.WorkplaceCreateRequest;
import ru.irlix.booking.dto.workplace.WorkplaceResponse;
import ru.irlix.booking.dto.workplace.WorkplaceUpdateRequest;
import ru.irlix.booking.entity.Workplace;
import ru.irlix.booking.mapper.WorkplaceMapper;
import ru.irlix.booking.repository.WorkplaceRepository;
import ru.irlix.booking.service.WorkplaceService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkplaceServiceImpl implements WorkplaceService {

    private final WorkplaceRepository workplaceRepository;
    private final WorkplaceMapper workplaceMapper;

    @Override
    public WorkplaceResponse getById(UUID id) {
        Workplace foundWorkplace = getWorkplaceWithNullCheck(id);
        log.info("Get workplace with id: {}: {}", id, foundWorkplace);

        return workplaceMapper.entityToResponse(foundWorkplace);
    }

    @Override
    public List<WorkplaceResponse> getAll() {
        return workplaceMapper.entityListToReponseList(workplaceRepository.findAll());
    }

    @Override
    public WorkplaceResponse save(@NonNull WorkplaceCreateRequest createRequest) {
        Workplace forSave = workplaceMapper.createRequestToEntity(createRequest);

        Workplace saved = workplaceRepository.save(forSave);
        log.info("Save workplace: {} ", saved);

        return workplaceMapper.entityToResponse(saved);
    }

    @Override
    public WorkplaceResponse update(UUID id, @NonNull WorkplaceUpdateRequest updateRequest) {
        Workplace currentWorkplace = getWorkplaceWithNullCheck(id);
        Workplace update = workplaceMapper.updateRequestToEntity(updateRequest);

        Optional.ofNullable(update.getNumber()).ifPresent(currentWorkplace::setNumber);
        Optional.ofNullable(update.getDescription()).ifPresent(currentWorkplace::setDescription);

        Workplace changed = workplaceRepository.save(currentWorkplace);
        log.info("Update workplace with id: {} : {}", id, changed);

        return workplaceMapper.entityToResponse(changed);
    }

    @Override
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
    private Workplace getWorkplaceWithNullCheck(UUID id) {
        return workplaceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workplace with id " + id + " not found"));
    }
}
