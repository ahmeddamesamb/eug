package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.PaiementFormationPrivee;
import sn.ugb.gir.repository.PaiementFormationPriveeRepository;
import sn.ugb.gir.repository.search.PaiementFormationPriveeSearchRepository;
import sn.ugb.gir.service.PaiementFormationPriveeService;
import sn.ugb.gir.service.dto.PaiementFormationPriveeDTO;
import sn.ugb.gir.service.mapper.PaiementFormationPriveeMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.PaiementFormationPrivee}.
 */
@Service
@Transactional
public class PaiementFormationPriveeServiceImpl implements PaiementFormationPriveeService {

    private final Logger log = LoggerFactory.getLogger(PaiementFormationPriveeServiceImpl.class);

    private final PaiementFormationPriveeRepository paiementFormationPriveeRepository;

    private final PaiementFormationPriveeMapper paiementFormationPriveeMapper;

    private final PaiementFormationPriveeSearchRepository paiementFormationPriveeSearchRepository;

    public PaiementFormationPriveeServiceImpl(
        PaiementFormationPriveeRepository paiementFormationPriveeRepository,
        PaiementFormationPriveeMapper paiementFormationPriveeMapper,
        PaiementFormationPriveeSearchRepository paiementFormationPriveeSearchRepository
    ) {
        this.paiementFormationPriveeRepository = paiementFormationPriveeRepository;
        this.paiementFormationPriveeMapper = paiementFormationPriveeMapper;
        this.paiementFormationPriveeSearchRepository = paiementFormationPriveeSearchRepository;
    }

    @Override
    public PaiementFormationPriveeDTO save(PaiementFormationPriveeDTO paiementFormationPriveeDTO) {
        log.debug("Request to save PaiementFormationPrivee : {}", paiementFormationPriveeDTO);
        PaiementFormationPrivee paiementFormationPrivee = paiementFormationPriveeMapper.toEntity(paiementFormationPriveeDTO);
        paiementFormationPrivee = paiementFormationPriveeRepository.save(paiementFormationPrivee);
        PaiementFormationPriveeDTO result = paiementFormationPriveeMapper.toDto(paiementFormationPrivee);
        paiementFormationPriveeSearchRepository.index(paiementFormationPrivee);
        return result;
    }

    @Override
    public PaiementFormationPriveeDTO update(PaiementFormationPriveeDTO paiementFormationPriveeDTO) {
        log.debug("Request to update PaiementFormationPrivee : {}", paiementFormationPriveeDTO);
        PaiementFormationPrivee paiementFormationPrivee = paiementFormationPriveeMapper.toEntity(paiementFormationPriveeDTO);
        paiementFormationPrivee = paiementFormationPriveeRepository.save(paiementFormationPrivee);
        PaiementFormationPriveeDTO result = paiementFormationPriveeMapper.toDto(paiementFormationPrivee);
        paiementFormationPriveeSearchRepository.index(paiementFormationPrivee);
        return result;
    }

    @Override
    public Optional<PaiementFormationPriveeDTO> partialUpdate(PaiementFormationPriveeDTO paiementFormationPriveeDTO) {
        log.debug("Request to partially update PaiementFormationPrivee : {}", paiementFormationPriveeDTO);

        return paiementFormationPriveeRepository
            .findById(paiementFormationPriveeDTO.getId())
            .map(existingPaiementFormationPrivee -> {
                paiementFormationPriveeMapper.partialUpdate(existingPaiementFormationPrivee, paiementFormationPriveeDTO);

                return existingPaiementFormationPrivee;
            })
            .map(paiementFormationPriveeRepository::save)
            .map(savedPaiementFormationPrivee -> {
                paiementFormationPriveeSearchRepository.index(savedPaiementFormationPrivee);
                return savedPaiementFormationPrivee;
            })
            .map(paiementFormationPriveeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaiementFormationPriveeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaiementFormationPrivees");
        return paiementFormationPriveeRepository.findAll(pageable).map(paiementFormationPriveeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaiementFormationPriveeDTO> findOne(Long id) {
        log.debug("Request to get PaiementFormationPrivee : {}", id);
        return paiementFormationPriveeRepository.findById(id).map(paiementFormationPriveeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaiementFormationPrivee : {}", id);
        paiementFormationPriveeRepository.deleteById(id);
        paiementFormationPriveeSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaiementFormationPriveeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaiementFormationPrivees for query {}", query);
        return paiementFormationPriveeSearchRepository.search(query, pageable).map(paiementFormationPriveeMapper::toDto);
    }
}
