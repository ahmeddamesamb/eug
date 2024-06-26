package sn.ugb.gp.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gp.domain.Enseignement;
import sn.ugb.gp.repository.EnseignementRepository;
import sn.ugb.gp.repository.search.EnseignementSearchRepository;
import sn.ugb.gp.service.EnseignementService;
import sn.ugb.gp.service.dto.EnseignementDTO;
import sn.ugb.gp.service.mapper.EnseignementMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gp.domain.Enseignement}.
 */
@Service
@Transactional
public class EnseignementServiceImpl implements EnseignementService {

    private final Logger log = LoggerFactory.getLogger(EnseignementServiceImpl.class);

    private final EnseignementRepository enseignementRepository;

    private final EnseignementMapper enseignementMapper;

    private final EnseignementSearchRepository enseignementSearchRepository;

    public EnseignementServiceImpl(
        EnseignementRepository enseignementRepository,
        EnseignementMapper enseignementMapper,
        EnseignementSearchRepository enseignementSearchRepository
    ) {
        this.enseignementRepository = enseignementRepository;
        this.enseignementMapper = enseignementMapper;
        this.enseignementSearchRepository = enseignementSearchRepository;
    }

    @Override
    public EnseignementDTO save(EnseignementDTO enseignementDTO) {
        log.debug("Request to save Enseignement : {}", enseignementDTO);
        Enseignement enseignement = enseignementMapper.toEntity(enseignementDTO);
        enseignement = enseignementRepository.save(enseignement);
        EnseignementDTO result = enseignementMapper.toDto(enseignement);
        enseignementSearchRepository.index(enseignement);
        return result;
    }

    @Override
    public EnseignementDTO update(EnseignementDTO enseignementDTO) {
        log.debug("Request to update Enseignement : {}", enseignementDTO);
        Enseignement enseignement = enseignementMapper.toEntity(enseignementDTO);
        enseignement = enseignementRepository.save(enseignement);
        EnseignementDTO result = enseignementMapper.toDto(enseignement);
        enseignementSearchRepository.index(enseignement);
        return result;
    }

    @Override
    public Optional<EnseignementDTO> partialUpdate(EnseignementDTO enseignementDTO) {
        log.debug("Request to partially update Enseignement : {}", enseignementDTO);

        return enseignementRepository
            .findById(enseignementDTO.getId())
            .map(existingEnseignement -> {
                enseignementMapper.partialUpdate(existingEnseignement, enseignementDTO);

                return existingEnseignement;
            })
            .map(enseignementRepository::save)
            .map(savedEnseignement -> {
                enseignementSearchRepository.index(savedEnseignement);
                return savedEnseignement;
            })
            .map(enseignementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnseignementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Enseignements");
        return enseignementRepository.findAll(pageable).map(enseignementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnseignementDTO> findOne(Long id) {
        log.debug("Request to get Enseignement : {}", id);
        return enseignementRepository.findById(id).map(enseignementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Enseignement : {}", id);
        enseignementRepository.deleteById(id);
        enseignementSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnseignementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Enseignements for query {}", query);
        return enseignementSearchRepository.search(query, pageable).map(enseignementMapper::toDto);
    }
}
