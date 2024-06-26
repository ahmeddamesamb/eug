package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.ProcessusInscriptionAdministrative;
import sn.ugb.gir.repository.ProcessusInscriptionAdministrativeRepository;
import sn.ugb.gir.repository.search.ProcessusInscriptionAdministrativeSearchRepository;
import sn.ugb.gir.service.ProcessusInscriptionAdministrativeService;
import sn.ugb.gir.service.dto.ProcessusInscriptionAdministrativeDTO;
import sn.ugb.gir.service.mapper.ProcessusInscriptionAdministrativeMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.ProcessusInscriptionAdministrative}.
 */
@Service
@Transactional
public class ProcessusInscriptionAdministrativeServiceImpl implements ProcessusInscriptionAdministrativeService {

    private final Logger log = LoggerFactory.getLogger(ProcessusInscriptionAdministrativeServiceImpl.class);

    private final ProcessusInscriptionAdministrativeRepository processusInscriptionAdministrativeRepository;

    private final ProcessusInscriptionAdministrativeMapper processusInscriptionAdministrativeMapper;

    private final ProcessusInscriptionAdministrativeSearchRepository processusInscriptionAdministrativeSearchRepository;

    public ProcessusInscriptionAdministrativeServiceImpl(
        ProcessusInscriptionAdministrativeRepository processusInscriptionAdministrativeRepository,
        ProcessusInscriptionAdministrativeMapper processusInscriptionAdministrativeMapper,
        ProcessusInscriptionAdministrativeSearchRepository processusInscriptionAdministrativeSearchRepository
    ) {
        this.processusInscriptionAdministrativeRepository = processusInscriptionAdministrativeRepository;
        this.processusInscriptionAdministrativeMapper = processusInscriptionAdministrativeMapper;
        this.processusInscriptionAdministrativeSearchRepository = processusInscriptionAdministrativeSearchRepository;
    }

    @Override
    public ProcessusInscriptionAdministrativeDTO save(ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO) {
        log.debug("Request to save ProcessusInscriptionAdministrative : {}", processusInscriptionAdministrativeDTO);
        ProcessusInscriptionAdministrative processusInscriptionAdministrative = processusInscriptionAdministrativeMapper.toEntity(
            processusInscriptionAdministrativeDTO
        );
        processusInscriptionAdministrative = processusInscriptionAdministrativeRepository.save(processusInscriptionAdministrative);
        ProcessusInscriptionAdministrativeDTO result = processusInscriptionAdministrativeMapper.toDto(processusInscriptionAdministrative);
        processusInscriptionAdministrativeSearchRepository.index(processusInscriptionAdministrative);
        return result;
    }

    @Override
    public ProcessusInscriptionAdministrativeDTO update(ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO) {
        log.debug("Request to update ProcessusInscriptionAdministrative : {}", processusInscriptionAdministrativeDTO);
        ProcessusInscriptionAdministrative processusInscriptionAdministrative = processusInscriptionAdministrativeMapper.toEntity(
            processusInscriptionAdministrativeDTO
        );
        processusInscriptionAdministrative = processusInscriptionAdministrativeRepository.save(processusInscriptionAdministrative);
        ProcessusInscriptionAdministrativeDTO result = processusInscriptionAdministrativeMapper.toDto(processusInscriptionAdministrative);
        processusInscriptionAdministrativeSearchRepository.index(processusInscriptionAdministrative);
        return result;
    }

    @Override
    public Optional<ProcessusInscriptionAdministrativeDTO> partialUpdate(
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO
    ) {
        log.debug("Request to partially update ProcessusInscriptionAdministrative : {}", processusInscriptionAdministrativeDTO);

        return processusInscriptionAdministrativeRepository
            .findById(processusInscriptionAdministrativeDTO.getId())
            .map(existingProcessusInscriptionAdministrative -> {
                processusInscriptionAdministrativeMapper.partialUpdate(
                    existingProcessusInscriptionAdministrative,
                    processusInscriptionAdministrativeDTO
                );

                return existingProcessusInscriptionAdministrative;
            })
            .map(processusInscriptionAdministrativeRepository::save)
            .map(savedProcessusInscriptionAdministrative -> {
                processusInscriptionAdministrativeSearchRepository.index(savedProcessusInscriptionAdministrative);
                return savedProcessusInscriptionAdministrative;
            })
            .map(processusInscriptionAdministrativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcessusInscriptionAdministrativeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProcessusInscriptionAdministratives");
        return processusInscriptionAdministrativeRepository.findAll(pageable).map(processusInscriptionAdministrativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcessusInscriptionAdministrativeDTO> findOne(Long id) {
        log.debug("Request to get ProcessusInscriptionAdministrative : {}", id);
        return processusInscriptionAdministrativeRepository.findById(id).map(processusInscriptionAdministrativeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProcessusInscriptionAdministrative : {}", id);
        processusInscriptionAdministrativeRepository.deleteById(id);
        processusInscriptionAdministrativeSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcessusInscriptionAdministrativeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProcessusInscriptionAdministratives for query {}", query);
        return processusInscriptionAdministrativeSearchRepository
            .search(query, pageable)
            .map(processusInscriptionAdministrativeMapper::toDto);
    }
}
