package ru.irlix.booking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.booking.dto.office.OfficeCreateRequest;
import ru.irlix.booking.dto.office.OfficeResponse;
import ru.irlix.booking.dto.office.OfficeUpdateRequest;
import ru.irlix.booking.entity.Office;
import ru.irlix.booking.mapper.OfficeMapper;
import ru.irlix.booking.repository.OfficeRepository;
import ru.irlix.booking.service.OfficeService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;
    private final OfficeMapper officeMapper;

    @Override
    public OfficeResponse getById(UUID id) {
        Office foundOffice = getOfficeWithNullCheck(id);
        log.info("Get office with id: {} : {}", id, foundOffice);
        return officeMapper.entityToResponse(foundOffice);
    }

    @Override
    public List<OfficeResponse> getAll() {
        return officeMapper.entityListToResponseList(officeRepository.findAll());
    }

    @Override
    public OfficeResponse save(@NonNull OfficeCreateRequest createRequest) {
        Office createdOffice = officeMapper.createRequestToEntity(createRequest);

        Office savedOffice = officeRepository.save(createdOffice);
        log.info("Created office : {}", savedOffice);

        return officeMapper.entityToResponse(savedOffice);
    }

    @Override
    public OfficeResponse update(UUID id, @NonNull OfficeUpdateRequest updateRequest) {
        Office currentOffice = getOfficeWithNullCheck(id);
        Office update = officeMapper.updateRequestToEntity(updateRequest);

        Optional.ofNullable(update.getName()).ifPresent(currentOffice::setName);
        Optional.ofNullable(update.getAddress()).ifPresent(currentOffice::setAddress);

        Office changedOffice = officeRepository.save(currentOffice);
        log.info("Updated office with id: {} : {}", id, changedOffice);

        return officeMapper.entityToResponse(changedOffice);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        officeRepository.changeOfficeIsDelete(id, true);
        log.info("Deleted office with id : {}", id);
    }

    /**
     * Получить офис с проверкой на null
     *
     * @param id - id офиса
     * @return - найденный офис
     */
    @Override
    public Office getOfficeWithNullCheck(UUID id) {
        return officeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Office with id " + id + " not found"));
    }
}
