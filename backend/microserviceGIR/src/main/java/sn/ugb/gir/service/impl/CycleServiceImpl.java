package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Cycle;
import sn.ugb.gir.repository.CycleRepository;
import sn.ugb.gir.repository.search.CycleSearchRepository;
import sn.ugb.gir.service.CycleService;
import sn.ugb.gir.service.dto.CycleDTO;
import sn.ugb.gir.service.mapper.CycleMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Cycle}.
 */
@Service
@Transactional
public class CycleServiceImpl implements CycleService {

    private final Logger log = LoggerFactory.getLogger(CycleServiceImpl.class);

    private final CycleRepository cycleRepository;

    private final CycleMapper cycleMapper;

    private final CycleSearchRepository cycleSearchRepository;

    public CycleServiceImpl(CycleRepository cycleRepository, CycleMapper cycleMapper, CycleSearchRepository cycleSearchRepository) {
        this.cycleRepository = cycleRepository;
        this.cycleMapper = cycleMapper;
        this.cycleSearchRepository = cycleSearchRepository;
    }

    @Override
    public CycleDTO save(CycleDTO cycleDTO) {
        log.debug("Request to save Cycle : {}", cycleDTO);
        Cycle cycle = cycleMapper.toEntity(cycleDTO);
        cycle = cycleRepository.save(cycle);
        CycleDTO result = cycleMapper.toDto(cycle);
        cycleSearchRepository.index(cycle);
        return result;
    }

    @Override
    public CycleDTO update(CycleDTO cycleDTO) {
        log.debug("Request to update Cycle : {}", cycleDTO);
        Cycle cycle = cycleMapper.toEntity(cycleDTO);
        cycle = cycleRepository.save(cycle);
        CycleDTO result = cycleMapper.toDto(cycle);
        cycleSearchRepository.index(cycle);
        return result;
    }

    @Override
    public Optional<CycleDTO> partialUpdate(CycleDTO cycleDTO) {
        log.debug("Request to partially update Cycle : {}", cycleDTO);

        return cycleRepository
            .findById(cycleDTO.getId())
            .map(existingCycle -> {
                cycleMapper.partialUpdate(existingCycle, cycleDTO);

                return existingCycle;
            })
            .map(cycleRepository::save)
            .map(savedCycle -> {
                cycleSearchRepository.index(savedCycle);
                return savedCycle;
            })
            .map(cycleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CycleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cycles");
        return cycleRepository.findAll(pageable).map(cycleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CycleDTO> findOne(Long id) {
        log.debug("Request to get Cycle : {}", id);
        return cycleRepository.findById(id).map(cycleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cycle : {}", id);
        cycleRepository.deleteById(id);
        cycleSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CycleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cycles for query {}", query);
        return cycleSearchRepository.search(query, pageable).map(cycleMapper::toDto);
    }
}
