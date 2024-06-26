package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Operateur;
import sn.ugb.gir.repository.OperateurRepository;
import sn.ugb.gir.repository.search.OperateurSearchRepository;
import sn.ugb.gir.service.OperateurService;
import sn.ugb.gir.service.dto.OperateurDTO;
import sn.ugb.gir.service.mapper.OperateurMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Operateur}.
 */
@Service
@Transactional
public class OperateurServiceImpl implements OperateurService {

    private final Logger log = LoggerFactory.getLogger(OperateurServiceImpl.class);

    private final OperateurRepository operateurRepository;

    private final OperateurMapper operateurMapper;

    private final OperateurSearchRepository operateurSearchRepository;

    public OperateurServiceImpl(
        OperateurRepository operateurRepository,
        OperateurMapper operateurMapper,
        OperateurSearchRepository operateurSearchRepository
    ) {
        this.operateurRepository = operateurRepository;
        this.operateurMapper = operateurMapper;
        this.operateurSearchRepository = operateurSearchRepository;
    }

    @Override
    public OperateurDTO save(OperateurDTO operateurDTO) {
        log.debug("Request to save Operateur : {}", operateurDTO);
        Operateur operateur = operateurMapper.toEntity(operateurDTO);
        operateur = operateurRepository.save(operateur);
        OperateurDTO result = operateurMapper.toDto(operateur);
        operateurSearchRepository.index(operateur);
        return result;
    }

    @Override
    public OperateurDTO update(OperateurDTO operateurDTO) {
        log.debug("Request to update Operateur : {}", operateurDTO);
        Operateur operateur = operateurMapper.toEntity(operateurDTO);
        operateur = operateurRepository.save(operateur);
        OperateurDTO result = operateurMapper.toDto(operateur);
        operateurSearchRepository.index(operateur);
        return result;
    }

    @Override
    public Optional<OperateurDTO> partialUpdate(OperateurDTO operateurDTO) {
        log.debug("Request to partially update Operateur : {}", operateurDTO);

        return operateurRepository
            .findById(operateurDTO.getId())
            .map(existingOperateur -> {
                operateurMapper.partialUpdate(existingOperateur, operateurDTO);

                return existingOperateur;
            })
            .map(operateurRepository::save)
            .map(savedOperateur -> {
                operateurSearchRepository.index(savedOperateur);
                return savedOperateur;
            })
            .map(operateurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OperateurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Operateurs");
        return operateurRepository.findAll(pageable).map(operateurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OperateurDTO> findOne(Long id) {
        log.debug("Request to get Operateur : {}", id);
        return operateurRepository.findById(id).map(operateurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Operateur : {}", id);
        operateurRepository.deleteById(id);
        operateurSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OperateurDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Operateurs for query {}", query);
        return operateurSearchRepository.search(query, pageable).map(operateurMapper::toDto);
    }
}
