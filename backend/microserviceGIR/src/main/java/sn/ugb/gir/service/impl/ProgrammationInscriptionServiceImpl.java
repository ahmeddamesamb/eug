package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.ProgrammationInscription;
import sn.ugb.gir.repository.ProgrammationInscriptionRepository;
import sn.ugb.gir.repository.search.ProgrammationInscriptionSearchRepository;
import sn.ugb.gir.service.ProgrammationInscriptionService;
import sn.ugb.gir.service.dto.ProgrammationInscriptionDTO;
import sn.ugb.gir.service.mapper.ProgrammationInscriptionMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.ProgrammationInscription}.
 */
@Service
@Transactional
public class ProgrammationInscriptionServiceImpl implements ProgrammationInscriptionService {

    private final Logger log = LoggerFactory.getLogger(ProgrammationInscriptionServiceImpl.class);

    private final ProgrammationInscriptionRepository programmationInscriptionRepository;

    private final ProgrammationInscriptionMapper programmationInscriptionMapper;

    private final ProgrammationInscriptionSearchRepository programmationInscriptionSearchRepository;

    public ProgrammationInscriptionServiceImpl(
        ProgrammationInscriptionRepository programmationInscriptionRepository,
        ProgrammationInscriptionMapper programmationInscriptionMapper,
        ProgrammationInscriptionSearchRepository programmationInscriptionSearchRepository
    ) {
        this.programmationInscriptionRepository = programmationInscriptionRepository;
        this.programmationInscriptionMapper = programmationInscriptionMapper;
        this.programmationInscriptionSearchRepository = programmationInscriptionSearchRepository;
    }

    @Override
    public ProgrammationInscriptionDTO save(ProgrammationInscriptionDTO programmationInscriptionDTO) {
        log.debug("Request to save ProgrammationInscription : {}", programmationInscriptionDTO);
        ProgrammationInscription programmationInscription = programmationInscriptionMapper.toEntity(programmationInscriptionDTO);
        programmationInscription = programmationInscriptionRepository.save(programmationInscription);
        ProgrammationInscriptionDTO result = programmationInscriptionMapper.toDto(programmationInscription);
        programmationInscriptionSearchRepository.index(programmationInscription);
        return result;
    }

    @Override
    public ProgrammationInscriptionDTO update(ProgrammationInscriptionDTO programmationInscriptionDTO) {
        log.debug("Request to update ProgrammationInscription : {}", programmationInscriptionDTO);
        ProgrammationInscription programmationInscription = programmationInscriptionMapper.toEntity(programmationInscriptionDTO);
        programmationInscription = programmationInscriptionRepository.save(programmationInscription);
        ProgrammationInscriptionDTO result = programmationInscriptionMapper.toDto(programmationInscription);
        programmationInscriptionSearchRepository.index(programmationInscription);
        return result;
    }

    @Override
    public Optional<ProgrammationInscriptionDTO> partialUpdate(ProgrammationInscriptionDTO programmationInscriptionDTO) {
        log.debug("Request to partially update ProgrammationInscription : {}", programmationInscriptionDTO);

        return programmationInscriptionRepository
            .findById(programmationInscriptionDTO.getId())
            .map(existingProgrammationInscription -> {
                programmationInscriptionMapper.partialUpdate(existingProgrammationInscription, programmationInscriptionDTO);

                return existingProgrammationInscription;
            })
            .map(programmationInscriptionRepository::save)
            .map(savedProgrammationInscription -> {
                programmationInscriptionSearchRepository.index(savedProgrammationInscription);
                return savedProgrammationInscription;
            })
            .map(programmationInscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProgrammationInscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProgrammationInscriptions");
        return programmationInscriptionRepository.findAll(pageable).map(programmationInscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProgrammationInscriptionDTO> findOne(Long id) {
        log.debug("Request to get ProgrammationInscription : {}", id);
        return programmationInscriptionRepository.findById(id).map(programmationInscriptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProgrammationInscription : {}", id);
        programmationInscriptionRepository.deleteById(id);
        programmationInscriptionSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProgrammationInscriptionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProgrammationInscriptions for query {}", query);
        return programmationInscriptionSearchRepository.search(query, pageable).map(programmationInscriptionMapper::toDto);
    }
}
