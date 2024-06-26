package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.DisciplineSportive;
import sn.ugb.gir.repository.DisciplineSportiveRepository;
import sn.ugb.gir.repository.search.DisciplineSportiveSearchRepository;
import sn.ugb.gir.service.DisciplineSportiveService;
import sn.ugb.gir.service.dto.DisciplineSportiveDTO;
import sn.ugb.gir.service.mapper.DisciplineSportiveMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.DisciplineSportive}.
 */
@Service
@Transactional
public class DisciplineSportiveServiceImpl implements DisciplineSportiveService {

    private final Logger log = LoggerFactory.getLogger(DisciplineSportiveServiceImpl.class);

    private final DisciplineSportiveRepository disciplineSportiveRepository;

    private final DisciplineSportiveMapper disciplineSportiveMapper;

    private final DisciplineSportiveSearchRepository disciplineSportiveSearchRepository;

    public DisciplineSportiveServiceImpl(
        DisciplineSportiveRepository disciplineSportiveRepository,
        DisciplineSportiveMapper disciplineSportiveMapper,
        DisciplineSportiveSearchRepository disciplineSportiveSearchRepository
    ) {
        this.disciplineSportiveRepository = disciplineSportiveRepository;
        this.disciplineSportiveMapper = disciplineSportiveMapper;
        this.disciplineSportiveSearchRepository = disciplineSportiveSearchRepository;
    }

    @Override
    public DisciplineSportiveDTO save(DisciplineSportiveDTO disciplineSportiveDTO) {
        log.debug("Request to save DisciplineSportive : {}", disciplineSportiveDTO);
        DisciplineSportive disciplineSportive = disciplineSportiveMapper.toEntity(disciplineSportiveDTO);
        disciplineSportive = disciplineSportiveRepository.save(disciplineSportive);
        DisciplineSportiveDTO result = disciplineSportiveMapper.toDto(disciplineSportive);
        disciplineSportiveSearchRepository.index(disciplineSportive);
        return result;
    }

    @Override
    public DisciplineSportiveDTO update(DisciplineSportiveDTO disciplineSportiveDTO) {
        log.debug("Request to update DisciplineSportive : {}", disciplineSportiveDTO);
        DisciplineSportive disciplineSportive = disciplineSportiveMapper.toEntity(disciplineSportiveDTO);
        disciplineSportive = disciplineSportiveRepository.save(disciplineSportive);
        DisciplineSportiveDTO result = disciplineSportiveMapper.toDto(disciplineSportive);
        disciplineSportiveSearchRepository.index(disciplineSportive);
        return result;
    }

    @Override
    public Optional<DisciplineSportiveDTO> partialUpdate(DisciplineSportiveDTO disciplineSportiveDTO) {
        log.debug("Request to partially update DisciplineSportive : {}", disciplineSportiveDTO);

        return disciplineSportiveRepository
            .findById(disciplineSportiveDTO.getId())
            .map(existingDisciplineSportive -> {
                disciplineSportiveMapper.partialUpdate(existingDisciplineSportive, disciplineSportiveDTO);

                return existingDisciplineSportive;
            })
            .map(disciplineSportiveRepository::save)
            .map(savedDisciplineSportive -> {
                disciplineSportiveSearchRepository.index(savedDisciplineSportive);
                return savedDisciplineSportive;
            })
            .map(disciplineSportiveMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DisciplineSportiveDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DisciplineSportives");
        return disciplineSportiveRepository.findAll(pageable).map(disciplineSportiveMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DisciplineSportiveDTO> findOne(Long id) {
        log.debug("Request to get DisciplineSportive : {}", id);
        return disciplineSportiveRepository.findById(id).map(disciplineSportiveMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DisciplineSportive : {}", id);
        disciplineSportiveRepository.deleteById(id);
        disciplineSportiveSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DisciplineSportiveDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DisciplineSportives for query {}", query);
        return disciplineSportiveSearchRepository.search(query, pageable).map(disciplineSportiveMapper::toDto);
    }
}
