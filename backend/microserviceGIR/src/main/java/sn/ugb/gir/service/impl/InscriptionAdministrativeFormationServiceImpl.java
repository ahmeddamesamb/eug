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
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

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


    private void appliquerReglesDeGestion(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO) {

        if (!resultatsDeDeliberationSontDisponibles()) {
            throw new BadRequestAlertException("Les résultats de délibérations ne sont pas disponibles", ENTITY_NAME, "nullUserProfile");
        }

        if (!etudiantEstAutoriseAInscription()) {
            throw new BadRequestAlertException("L’étudiant n’est pas autorisé à s’inscrire.", ENTITY_NAME, "nullUserProfile");
        }

        if (!programmationEstOuverte()) {
            throw new BadRequestAlertException("La programmation n'est pas ouverte.", ENTITY_NAME, "nullUserProfile");
        }

        if (etudiantReprendSesEtudes()) {
            throw new BadRequestAlertException("L’étudiant ne peut pas démarrer son inscription en ligne en cas de reprise d’étude.", ENTITY_NAME, "nullUserProfile");
        }

        if (etudiantEstEnPositionDeCartouche(inscriptionAdministrativeFormationDTO)) {
            throw new BadRequestAlertException("L’étudiant ne doit pas dépasser plus de deux inscriptions sur un niveau donné.", ENTITY_NAME, "nullUserProfile");
        }

        if (etudiantEstAncienEtudiant() && !etudiantAFinaliseInscriptionAnneeDerniere()) {
            throw new BadRequestAlertException("L’étudiant (un ancien) doit finaliser son inscription de l’année dernière.", ENTITY_NAME, "nullUserProfile");
        }
    }

    private boolean resultatsDeDeliberationSontDisponibles() {
        // Implémenter la logique pour RG1
        return true; // Placeholder
    }

    private boolean etudiantEstAutoriseAInscription() {
        // Implémenter la logique pour RG2
        return true; // Placeholder
    }

    private boolean programmationEstOuverte() {
        // Implémenter la logique pour RG3
        return true; // Placeholder
    }

    private boolean etudiantReprendSesEtudes() {
        // Implémenter la logique pour RG4
        return false; // Placeholder
    }

    private boolean etudiantEstEnPositionDeCartouche(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO) {
        // Implémenter la logique pour RG5
        return false; // Placeholder
    }

    private boolean etudiantEstAncienEtudiant() {
        // Implémenter la logique pour RG6
        return false; // Placeholder
    }

    private boolean etudiantAFinaliseInscriptionAnneeDerniere() {
        // Implémenter la logique pour RG6
        return true; // Placeholder
    }
}

