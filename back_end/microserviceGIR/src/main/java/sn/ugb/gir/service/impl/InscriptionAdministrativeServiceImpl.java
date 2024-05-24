package sn.ugb.gir.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.InscriptionAdministrative;
import sn.ugb.gir.repository.InscriptionAdministrativeRepository;
import sn.ugb.gir.service.InscriptionAdministrativeService;
import sn.ugb.gir.service.dto.InscriptionAdministrativeDTO;
import sn.ugb.gir.service.mapper.InscriptionAdministrativeMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.InscriptionAdministrative}.
 */
@Service
@Transactional
public class InscriptionAdministrativeServiceImpl implements InscriptionAdministrativeService {

    private final Logger log = LoggerFactory.getLogger(InscriptionAdministrativeServiceImpl.class);

    private final InscriptionAdministrativeRepository inscriptionAdministrativeRepository;

    private final InscriptionAdministrativeMapper inscriptionAdministrativeMapper;

    public InscriptionAdministrativeServiceImpl(
        InscriptionAdministrativeRepository inscriptionAdministrativeRepository,
        InscriptionAdministrativeMapper inscriptionAdministrativeMapper
    ) {
        this.inscriptionAdministrativeRepository = inscriptionAdministrativeRepository;
        this.inscriptionAdministrativeMapper = inscriptionAdministrativeMapper;
    }

    @Override
    public InscriptionAdministrativeDTO save(InscriptionAdministrativeDTO inscriptionAdministrativeDTO) {
        log.debug("Request to save InscriptionAdministrative : {}", inscriptionAdministrativeDTO);
        InscriptionAdministrative inscriptionAdministrative = inscriptionAdministrativeMapper.toEntity(inscriptionAdministrativeDTO);
        inscriptionAdministrative = inscriptionAdministrativeRepository.save(inscriptionAdministrative);
        return inscriptionAdministrativeMapper.toDto(inscriptionAdministrative);
    }

    @Override
    public InscriptionAdministrativeDTO update(InscriptionAdministrativeDTO inscriptionAdministrativeDTO) {
        log.debug("Request to update InscriptionAdministrative : {}", inscriptionAdministrativeDTO);
        InscriptionAdministrative inscriptionAdministrative = inscriptionAdministrativeMapper.toEntity(inscriptionAdministrativeDTO);
        inscriptionAdministrative = inscriptionAdministrativeRepository.save(inscriptionAdministrative);
        return inscriptionAdministrativeMapper.toDto(inscriptionAdministrative);
    }

    @Override
    public Optional<InscriptionAdministrativeDTO> partialUpdate(InscriptionAdministrativeDTO inscriptionAdministrativeDTO) {
        log.debug("Request to partially update InscriptionAdministrative : {}", inscriptionAdministrativeDTO);

        return inscriptionAdministrativeRepository
            .findById(inscriptionAdministrativeDTO.getId())
            .map(existingInscriptionAdministrative -> {
                inscriptionAdministrativeMapper.partialUpdate(existingInscriptionAdministrative, inscriptionAdministrativeDTO);

                return existingInscriptionAdministrative;
            })
            .map(inscriptionAdministrativeRepository::save)
            .map(inscriptionAdministrativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InscriptionAdministrativeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InscriptionAdministratives");
        return inscriptionAdministrativeRepository.findAll(pageable).map(inscriptionAdministrativeMapper::toDto);
    }

    /**
     *  Get all the inscriptionAdministratives where ProcessusDinscriptionAdministrative is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InscriptionAdministrativeDTO> findAllWhereProcessusDinscriptionAdministrativeIsNull() {
        log.debug("Request to get all inscriptionAdministratives where ProcessusDinscriptionAdministrative is null");
        return StreamSupport
            .stream(inscriptionAdministrativeRepository.findAll().spliterator(), false)
            .filter(inscriptionAdministrative -> inscriptionAdministrative.getProcessusDinscriptionAdministrative() == null)
            .map(inscriptionAdministrativeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InscriptionAdministrativeDTO> findOne(Long id) {
        log.debug("Request to get InscriptionAdministrative : {}", id);
        return inscriptionAdministrativeRepository.findById(id).map(inscriptionAdministrativeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InscriptionAdministrative : {}", id);
        inscriptionAdministrativeRepository.deleteById(id);
    }
}
