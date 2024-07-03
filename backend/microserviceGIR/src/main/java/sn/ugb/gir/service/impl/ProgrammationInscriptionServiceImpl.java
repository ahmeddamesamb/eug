package sn.ugb.gir.service.impl;

import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Campagne;
import sn.ugb.gir.domain.ProgrammationInscription;
import sn.ugb.gir.repository.CampagneRepository;
import sn.ugb.gir.repository.ProgrammationInscriptionRepository;
import sn.ugb.gir.repository.search.ProgrammationInscriptionSearchRepository;
import sn.ugb.gir.service.ProgrammationInscriptionService;
import sn.ugb.gir.service.dto.ProgrammationInscriptionDTO;
import sn.ugb.gir.service.mapper.ProgrammationInscriptionMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import org.springframework.scheduling.annotation.Scheduled;

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

    private static final String ENTITY_NAME = "microserviceGirProgrammationInscription";

    private  final CampagneRepository campagneRepository;

    public ProgrammationInscriptionServiceImpl(
        ProgrammationInscriptionRepository programmationInscriptionRepository,
        ProgrammationInscriptionMapper programmationInscriptionMapper,
        ProgrammationInscriptionSearchRepository programmationInscriptionSearchRepository,
        CampagneRepository campagneRepository
    ) {
        this.programmationInscriptionRepository = programmationInscriptionRepository;
        this.programmationInscriptionMapper = programmationInscriptionMapper;
        this.programmationInscriptionSearchRepository = programmationInscriptionSearchRepository;
        this.campagneRepository = campagneRepository;
    }

    @Override
    public ProgrammationInscriptionDTO save(ProgrammationInscriptionDTO programmationInscriptionDTO) {
        log.debug("Request to save ProgrammationInscription : {}", programmationInscriptionDTO);
        validateProgrammationInscription(programmationInscriptionDTO);
        ProgrammationInscription programmationInscription = programmationInscriptionMapper.toEntity(programmationInscriptionDTO);
        programmationInscription = programmationInscriptionRepository.save(programmationInscription);
        ProgrammationInscriptionDTO result = programmationInscriptionMapper.toDto(programmationInscription);
        programmationInscriptionSearchRepository.index(programmationInscription);
        return result;
    }

    @Override
    public ProgrammationInscriptionDTO update(ProgrammationInscriptionDTO programmationInscriptionDTO) {
        log.debug("Request to update ProgrammationInscription : {}", programmationInscriptionDTO);
        validateProgrammationInscription(programmationInscriptionDTO);
        ProgrammationInscription programmationInscription = programmationInscriptionMapper.toEntity(programmationInscriptionDTO);
        programmationInscription = programmationInscriptionRepository.save(programmationInscription);
        ProgrammationInscriptionDTO result = programmationInscriptionMapper.toDto(programmationInscription);
        programmationInscriptionSearchRepository.index(programmationInscription);
        return result;
    }

    @Override
    public Optional<ProgrammationInscriptionDTO> partialUpdate(ProgrammationInscriptionDTO programmationInscriptionDTO) {
        log.debug("Request to partially update ProgrammationInscription : {}", programmationInscriptionDTO);
        validateProgrammationInscription(programmationInscriptionDTO);
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

    private void validateProgrammationInscription(ProgrammationInscriptionDTO programmationInscriptionDTO) {
        // RG1: Tous les champs sont obligatoires
        if (programmationInscriptionDTO.getLibelleProgrammation() == null ||
            programmationInscriptionDTO.getDateDebutProgrammation() == null ||
            programmationInscriptionDTO.getDateFinProgrammation() == null ||
            programmationInscriptionDTO.getDateForclosClasse() == null ||
            programmationInscriptionDTO.getAnneeAcademique() == null ||
            programmationInscriptionDTO.getFormation() == null ||
            programmationInscriptionDTO.getCampagne() == null) {
            throw new BadRequestAlertException("Tous les champs sont obligatoires.",ENTITY_NAME,"requiredFields");
        }

        if (programmationInscriptionDTO.getLibelleProgrammation().isBlank()) {
            throw new BadRequestAlertException("Le libellé ne peut pas être vide.", ENTITY_NAME, "libelleProgrammationNotNull");
        }

        // RG2: La date de début ne doit pas être postérieure à la date de fin
        if (programmationInscriptionDTO.getDateDebutProgrammation().isAfter(programmationInscriptionDTO.getDateFinProgrammation())) {
            throw new BadRequestAlertException("La date de début ne doit pas être postérieure à la date de fin.",ENTITY_NAME,"dateDebutNotPosteriorDateFin");
        }

        // RG3: On ne peut pas programmer une formation/campagne dans le passé
        if (programmationInscriptionDTO.getDateDebutProgrammation().isBefore(LocalDate.now()) ||
            programmationInscriptionDTO.getDateFinProgrammation().isBefore(LocalDate.now())) {
            throw new BadRequestAlertException("On ne peut pas programmer une formation/campagne dans le passé.",ENTITY_NAME,"noFormationCampaignInPast");
        }

        // RG4: La date forclos est toujours postérieure à la date de fin de la programmation
        if (programmationInscriptionDTO.getDateForclosClasse().isBefore(programmationInscriptionDTO.getDateFinProgrammation())) {
            throw new BadRequestAlertException("La date forclos doit être postérieure à la date de fin.",ENTITY_NAME,"dateForclosNotPosteriorDateFin");
        }

        // RG5: La période de programmation doit être incluse dans la période de campagne
        Campagne campagne = campagneRepository.findById(programmationInscriptionDTO.getCampagne().getId())
            .orElseThrow(() ->  new BadRequestAlertException("Campagne non trouvee.",ENTITY_NAME,"campagneNotFound"));
        if (programmationInscriptionDTO.getDateDebutProgrammation().isBefore(campagne.getDateDebut()) ||
            programmationInscriptionDTO.getDateFinProgrammation().isAfter(campagne.getDateFin())) {
            throw new BadRequestAlertException("La période de programmation doit être incluse dans la période de la campagne.",ENTITY_NAME,"programmationWithinCampagnePeriod");
        }

        // RG6: La programmation d’une formation sur une période (date début - date de fin) doit être unique
        Optional<ProgrammationInscription> existingProgrammationInscription = programmationInscriptionRepository.findByFormation_IdAndCampagne_IdAndAnneeAcademique_IdAndDateDebutProgrammationAndDateFinProgrammation(
            programmationInscriptionDTO.getFormation().getId(),
            programmationInscriptionDTO.getCampagne().getId(),
            programmationInscriptionDTO.getAnneeAcademique().getId(),
            programmationInscriptionDTO.getDateDebutProgrammation(),
            programmationInscriptionDTO.getDateFinProgrammation()
        );
        if (existingProgrammationInscription.isPresent() && !existingProgrammationInscription.get().getId().equals(programmationInscriptionDTO.getId())) {
            throw new BadRequestAlertException("Une programmation pour cette formation sur cette période existe déjà.",ENTITY_NAME,"programmationInscriptionExist");
        }
    }
}
