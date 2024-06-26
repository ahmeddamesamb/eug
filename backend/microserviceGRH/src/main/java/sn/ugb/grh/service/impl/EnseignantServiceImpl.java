package sn.ugb.grh.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.grh.domain.Enseignant;
import sn.ugb.grh.repository.EnseignantRepository;
import sn.ugb.grh.repository.search.EnseignantSearchRepository;
import sn.ugb.grh.service.EnseignantService;
import sn.ugb.grh.service.dto.EnseignantDTO;
import sn.ugb.grh.service.mapper.EnseignantMapper;

/**
 * Service Implementation for managing {@link sn.ugb.grh.domain.Enseignant}.
 */
@Service
@Transactional
public class EnseignantServiceImpl implements EnseignantService {

    private final Logger log = LoggerFactory.getLogger(EnseignantServiceImpl.class);

    private final EnseignantRepository enseignantRepository;

    private final EnseignantMapper enseignantMapper;

    private final EnseignantSearchRepository enseignantSearchRepository;

    public EnseignantServiceImpl(
        EnseignantRepository enseignantRepository,
        EnseignantMapper enseignantMapper,
        EnseignantSearchRepository enseignantSearchRepository
    ) {
        this.enseignantRepository = enseignantRepository;
        this.enseignantMapper = enseignantMapper;
        this.enseignantSearchRepository = enseignantSearchRepository;
    }

    @Override
    public EnseignantDTO save(EnseignantDTO enseignantDTO) {
        log.debug("Request to save Enseignant : {}", enseignantDTO);
        Enseignant enseignant = enseignantMapper.toEntity(enseignantDTO);
        enseignant = enseignantRepository.save(enseignant);
        EnseignantDTO result = enseignantMapper.toDto(enseignant);
        enseignantSearchRepository.index(enseignant);
        return result;
    }

    @Override
    public EnseignantDTO update(EnseignantDTO enseignantDTO) {
        log.debug("Request to update Enseignant : {}", enseignantDTO);
        Enseignant enseignant = enseignantMapper.toEntity(enseignantDTO);
        enseignant = enseignantRepository.save(enseignant);
        EnseignantDTO result = enseignantMapper.toDto(enseignant);
        enseignantSearchRepository.index(enseignant);
        return result;
    }

    @Override
    public Optional<EnseignantDTO> partialUpdate(EnseignantDTO enseignantDTO) {
        log.debug("Request to partially update Enseignant : {}", enseignantDTO);

        return enseignantRepository
            .findById(enseignantDTO.getId())
            .map(existingEnseignant -> {
                enseignantMapper.partialUpdate(existingEnseignant, enseignantDTO);

                return existingEnseignant;
            })
            .map(enseignantRepository::save)
            .map(savedEnseignant -> {
                enseignantSearchRepository.index(savedEnseignant);
                return savedEnseignant;
            })
            .map(enseignantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnseignantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Enseignants");
        return enseignantRepository.findAll(pageable).map(enseignantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnseignantDTO> findOne(Long id) {
        log.debug("Request to get Enseignant : {}", id);
        return enseignantRepository.findById(id).map(enseignantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Enseignant : {}", id);
        enseignantRepository.deleteById(id);
        enseignantSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnseignantDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Enseignants for query {}", query);
        return enseignantSearchRepository.search(query, pageable).map(enseignantMapper::toDto);
    }
}
