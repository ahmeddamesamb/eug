package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.ProcessusDinscriptionAdministrative;
import sn.ugb.gir.repository.ProcessusDinscriptionAdministrativeRepository;
import sn.ugb.gir.service.ProcessusDinscriptionAdministrativeService;
import sn.ugb.gir.service.dto.ProcessusDinscriptionAdministrativeDTO;
import sn.ugb.gir.service.mapper.ProcessusDinscriptionAdministrativeMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.ProcessusDinscriptionAdministrative}.
 */
@Service
@Transactional
public class ProcessusDinscriptionAdministrativeServiceImpl implements ProcessusDinscriptionAdministrativeService {

    private final Logger log = LoggerFactory.getLogger(ProcessusDinscriptionAdministrativeServiceImpl.class);

    private final ProcessusDinscriptionAdministrativeRepository processusDinscriptionAdministrativeRepository;

    private final ProcessusDinscriptionAdministrativeMapper processusDinscriptionAdministrativeMapper;

    public ProcessusDinscriptionAdministrativeServiceImpl(
        ProcessusDinscriptionAdministrativeRepository processusDinscriptionAdministrativeRepository,
        ProcessusDinscriptionAdministrativeMapper processusDinscriptionAdministrativeMapper
    ) {
        this.processusDinscriptionAdministrativeRepository = processusDinscriptionAdministrativeRepository;
        this.processusDinscriptionAdministrativeMapper = processusDinscriptionAdministrativeMapper;
    }

    @Override
    public ProcessusDinscriptionAdministrativeDTO save(ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO) {
        log.debug("Request to save ProcessusDinscriptionAdministrative : {}", processusDinscriptionAdministrativeDTO);
        ProcessusDinscriptionAdministrative processusDinscriptionAdministrative = processusDinscriptionAdministrativeMapper.toEntity(
            processusDinscriptionAdministrativeDTO
        );
        processusDinscriptionAdministrative = processusDinscriptionAdministrativeRepository.save(processusDinscriptionAdministrative);
        return processusDinscriptionAdministrativeMapper.toDto(processusDinscriptionAdministrative);
    }

    @Override
    public ProcessusDinscriptionAdministrativeDTO update(ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO) {
        log.debug("Request to update ProcessusDinscriptionAdministrative : {}", processusDinscriptionAdministrativeDTO);
        ProcessusDinscriptionAdministrative processusDinscriptionAdministrative = processusDinscriptionAdministrativeMapper.toEntity(
            processusDinscriptionAdministrativeDTO
        );
        processusDinscriptionAdministrative = processusDinscriptionAdministrativeRepository.save(processusDinscriptionAdministrative);
        return processusDinscriptionAdministrativeMapper.toDto(processusDinscriptionAdministrative);
    }

    @Override
    public Optional<ProcessusDinscriptionAdministrativeDTO> partialUpdate(
        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO
    ) {
        log.debug("Request to partially update ProcessusDinscriptionAdministrative : {}", processusDinscriptionAdministrativeDTO);

        return processusDinscriptionAdministrativeRepository
            .findById(processusDinscriptionAdministrativeDTO.getId())
            .map(existingProcessusDinscriptionAdministrative -> {
                processusDinscriptionAdministrativeMapper.partialUpdate(
                    existingProcessusDinscriptionAdministrative,
                    processusDinscriptionAdministrativeDTO
                );

                return existingProcessusDinscriptionAdministrative;
            })
            .map(processusDinscriptionAdministrativeRepository::save)
            .map(processusDinscriptionAdministrativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcessusDinscriptionAdministrativeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProcessusDinscriptionAdministratives");
        return processusDinscriptionAdministrativeRepository.findAll(pageable).map(processusDinscriptionAdministrativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcessusDinscriptionAdministrativeDTO> findOne(Long id) {
        log.debug("Request to get ProcessusDinscriptionAdministrative : {}", id);
        return processusDinscriptionAdministrativeRepository.findById(id).map(processusDinscriptionAdministrativeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProcessusDinscriptionAdministrative : {}", id);
        processusDinscriptionAdministrativeRepository.deleteById(id);
    }
}
