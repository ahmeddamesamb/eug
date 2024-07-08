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
import sn.ugb.gir.repository.search.InscriptionAdministrativeSearchRepository;
import sn.ugb.gir.service.InscriptionAdministrativeService;
import sn.ugb.gir.service.dto.AnneeAcademiqueDTO;
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

    private final InscriptionAdministrativeSearchRepository inscriptionAdministrativeSearchRepository;

    public InscriptionAdministrativeServiceImpl(
        InscriptionAdministrativeRepository inscriptionAdministrativeRepository,
        InscriptionAdministrativeMapper inscriptionAdministrativeMapper,
        InscriptionAdministrativeSearchRepository inscriptionAdministrativeSearchRepository
    ) {
        this.inscriptionAdministrativeRepository = inscriptionAdministrativeRepository;
        this.inscriptionAdministrativeMapper = inscriptionAdministrativeMapper;
        this.inscriptionAdministrativeSearchRepository = inscriptionAdministrativeSearchRepository;
    }

    @Override
    public InscriptionAdministrativeDTO save(InscriptionAdministrativeDTO inscriptionAdministrativeDTO) {
        log.debug("Request to save InscriptionAdministrative : {}", inscriptionAdministrativeDTO);
        InscriptionAdministrative inscriptionAdministrative = inscriptionAdministrativeMapper.toEntity(inscriptionAdministrativeDTO);
        inscriptionAdministrative = inscriptionAdministrativeRepository.save(inscriptionAdministrative);
        InscriptionAdministrativeDTO result = inscriptionAdministrativeMapper.toDto(inscriptionAdministrative);
        inscriptionAdministrativeSearchRepository.index(inscriptionAdministrative);
        return result;
    }

    @Override
    public InscriptionAdministrativeDTO update(InscriptionAdministrativeDTO inscriptionAdministrativeDTO) {
        log.debug("Request to update InscriptionAdministrative : {}", inscriptionAdministrativeDTO);
        InscriptionAdministrative inscriptionAdministrative = inscriptionAdministrativeMapper.toEntity(inscriptionAdministrativeDTO);
        inscriptionAdministrative = inscriptionAdministrativeRepository.save(inscriptionAdministrative);
        InscriptionAdministrativeDTO result = inscriptionAdministrativeMapper.toDto(inscriptionAdministrative);
        inscriptionAdministrativeSearchRepository.index(inscriptionAdministrative);
        return result;
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
            .map(savedInscriptionAdministrative -> {
                inscriptionAdministrativeSearchRepository.index(savedInscriptionAdministrative);
                return savedInscriptionAdministrative;
            })
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
        inscriptionAdministrativeSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InscriptionAdministrativeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InscriptionAdministratives for query {}", query);
        return inscriptionAdministrativeSearchRepository.search(query, pageable).map(inscriptionAdministrativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public long countNouveauInscrits() {
        log.debug("Request to count InscriptionAdministratives where nouveauInscritYN is true");
        return inscriptionAdministrativeRepository.countByNouveauInscritYNTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long countNouveauInscritsByAnneeAcademique(Long anneeAcademiqueId) {
        log.debug("Request to count InscriptionAdministratives where nouveauInscritYN is true for anneeAcademiqueId: {}", anneeAcademiqueId);
        return inscriptionAdministrativeRepository.countByNouveauInscritYNTrueAndAnneeAcademiqueId(anneeAcademiqueId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countNouveauInscritsByAnneeAcademiqueEnCours() {
        log.debug("Request to count InscriptionAdministratives where nouveauInscritYN is true and anneeAcademique.enCoursYN is true");
        return inscriptionAdministrativeRepository.countByNouveauInscritYNTrueAndAnneeAcademiqueAnneeCouranteYNTrue();
    }



}
