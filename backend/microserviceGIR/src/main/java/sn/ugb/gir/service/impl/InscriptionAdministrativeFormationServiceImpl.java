package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.repository.InscriptionAdministrativeFormationRepository;
import sn.ugb.gir.repository.search.InscriptionAdministrativeFormationSearchRepository;
import sn.ugb.gir.service.InscriptionAdministrativeFormationService;
import sn.ugb.gir.service.dto.InscriptionAdministrativeFormationDTO;
import sn.ugb.gir.service.mapper.InscriptionAdministrativeFormationMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.InscriptionAdministrativeFormation}.
 */
@Service
@Transactional
public class InscriptionAdministrativeFormationServiceImpl implements InscriptionAdministrativeFormationService {

    private final Logger log = LoggerFactory.getLogger(InscriptionAdministrativeFormationServiceImpl.class);

    private final InscriptionAdministrativeFormationRepository inscriptionAdministrativeFormationRepository;

    private final InscriptionAdministrativeFormationMapper inscriptionAdministrativeFormationMapper;

    private final InscriptionAdministrativeFormationSearchRepository inscriptionAdministrativeFormationSearchRepository;

    public InscriptionAdministrativeFormationServiceImpl(
        InscriptionAdministrativeFormationRepository inscriptionAdministrativeFormationRepository,
        InscriptionAdministrativeFormationMapper inscriptionAdministrativeFormationMapper,
        InscriptionAdministrativeFormationSearchRepository inscriptionAdministrativeFormationSearchRepository
    ) {
        this.inscriptionAdministrativeFormationRepository = inscriptionAdministrativeFormationRepository;
        this.inscriptionAdministrativeFormationMapper = inscriptionAdministrativeFormationMapper;
        this.inscriptionAdministrativeFormationSearchRepository = inscriptionAdministrativeFormationSearchRepository;
    }

    @Override
    public InscriptionAdministrativeFormationDTO save(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO) {
        log.debug("Request to save InscriptionAdministrativeFormation : {}", inscriptionAdministrativeFormationDTO);
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation = inscriptionAdministrativeFormationMapper.toEntity(
            inscriptionAdministrativeFormationDTO
        );
        inscriptionAdministrativeFormation = inscriptionAdministrativeFormationRepository.save(inscriptionAdministrativeFormation);
        InscriptionAdministrativeFormationDTO result = inscriptionAdministrativeFormationMapper.toDto(inscriptionAdministrativeFormation);
        inscriptionAdministrativeFormationSearchRepository.index(inscriptionAdministrativeFormation);
        return result;
    }

    @Override
    public InscriptionAdministrativeFormationDTO update(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO) {
        log.debug("Request to update InscriptionAdministrativeFormation : {}", inscriptionAdministrativeFormationDTO);
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation = inscriptionAdministrativeFormationMapper.toEntity(
            inscriptionAdministrativeFormationDTO
        );
        inscriptionAdministrativeFormation = inscriptionAdministrativeFormationRepository.save(inscriptionAdministrativeFormation);
        InscriptionAdministrativeFormationDTO result = inscriptionAdministrativeFormationMapper.toDto(inscriptionAdministrativeFormation);
        inscriptionAdministrativeFormationSearchRepository.index(inscriptionAdministrativeFormation);
        return result;
    }

    @Override
    public Optional<InscriptionAdministrativeFormationDTO> partialUpdate(
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO
    ) {
        log.debug("Request to partially update InscriptionAdministrativeFormation : {}", inscriptionAdministrativeFormationDTO);

        return inscriptionAdministrativeFormationRepository
            .findById(inscriptionAdministrativeFormationDTO.getId())
            .map(existingInscriptionAdministrativeFormation -> {
                inscriptionAdministrativeFormationMapper.partialUpdate(
                    existingInscriptionAdministrativeFormation,
                    inscriptionAdministrativeFormationDTO
                );

                return existingInscriptionAdministrativeFormation;
            })
            .map(inscriptionAdministrativeFormationRepository::save)
            .map(savedInscriptionAdministrativeFormation -> {
                inscriptionAdministrativeFormationSearchRepository.index(savedInscriptionAdministrativeFormation);
                return savedInscriptionAdministrativeFormation;
            })
            .map(inscriptionAdministrativeFormationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InscriptionAdministrativeFormationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InscriptionAdministrativeFormations");
        return inscriptionAdministrativeFormationRepository.findAll(pageable).map(inscriptionAdministrativeFormationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InscriptionAdministrativeFormationDTO> findOne(Long id) {
        log.debug("Request to get InscriptionAdministrativeFormation : {}", id);
        return inscriptionAdministrativeFormationRepository.findById(id).map(inscriptionAdministrativeFormationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InscriptionAdministrativeFormation : {}", id);
        inscriptionAdministrativeFormationRepository.deleteById(id);
        inscriptionAdministrativeFormationSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InscriptionAdministrativeFormationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InscriptionAdministrativeFormations for query {}", query);
        return inscriptionAdministrativeFormationSearchRepository
            .search(query, pageable)
            .map(inscriptionAdministrativeFormationMapper::toDto);
    }
}
