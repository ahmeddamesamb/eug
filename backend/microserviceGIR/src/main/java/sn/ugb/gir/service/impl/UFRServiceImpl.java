package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.UFR;
import sn.ugb.gir.repository.UFRRepository;
import sn.ugb.gir.service.UFRService;
import sn.ugb.gir.service.dto.UFRDTO;
import sn.ugb.gir.service.mapper.UFRMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.UFR}.
 */
@Service
@Transactional
public class UFRServiceImpl implements UFRService {

    private final Logger log = LoggerFactory.getLogger(UFRServiceImpl.class);

    private final UFRRepository uFRRepository;

    private final UFRMapper uFRMapper;

    public UFRServiceImpl(UFRRepository uFRRepository, UFRMapper uFRMapper) {
        this.uFRRepository = uFRRepository;
        this.uFRMapper = uFRMapper;
    }

    @Override
    public UFRDTO save(UFRDTO uFRDTO) {
        log.debug("Request to save UFR : {}", uFRDTO);
        UFR uFR = uFRMapper.toEntity(uFRDTO);
        uFR = uFRRepository.save(uFR);
        return uFRMapper.toDto(uFR);
    }

    @Override
    public UFRDTO update(UFRDTO uFRDTO) {
        log.debug("Request to update UFR : {}", uFRDTO);
        UFR uFR = uFRMapper.toEntity(uFRDTO);
        uFR = uFRRepository.save(uFR);
        return uFRMapper.toDto(uFR);
    }

    @Override
    public Optional<UFRDTO> partialUpdate(UFRDTO uFRDTO) {
        log.debug("Request to partially update UFR : {}", uFRDTO);

        return uFRRepository
            .findById(uFRDTO.getId())
            .map(existingUFR -> {
                uFRMapper.partialUpdate(existingUFR, uFRDTO);

                return existingUFR;
            })
            .map(uFRRepository::save)
            .map(uFRMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UFRDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UFRS");
        return uFRRepository.findAll(pageable).map(uFRMapper::toDto);
    }

    public Page<UFRDTO> findAllWithEagerRelationships(Pageable pageable) {
        return uFRRepository.findAllWithEagerRelationships(pageable).map(uFRMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UFRDTO> findOne(Long id) {
        log.debug("Request to get UFR : {}", id);
        return uFRRepository.findOneWithEagerRelationships(id).map(uFRMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UFR : {}", id);
        uFRRepository.deleteById(id);
    }
}
