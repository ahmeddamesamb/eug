package sn.ugb.gir.service.impl;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.InscriptionAdministrative;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.domain.PaiementFrais;
import sn.ugb.gir.repository.InscriptionAdministrativeFormationRepository;
import sn.ugb.gir.repository.InscriptionAdministrativeRepository;
import sn.ugb.gir.repository.PaiementFraisRepository;
import sn.ugb.gir.repository.search.InscriptionAdministrativeFormationSearchRepository;
import sn.ugb.gir.service.InscriptionAdministrativeFormationService;
import sn.ugb.gir.service.dto.*;
import sn.ugb.gir.service.mapper.InscriptionAdministrativeFormationMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.InscriptionAdministrativeFormation}.
 */
@Service
@Transactional
public class InscriptionAdministrativeFormationServiceImpl implements InscriptionAdministrativeFormationService {

    private final Logger log = LoggerFactory.getLogger(InscriptionAdministrativeFormationServiceImpl.class);

    private static final String ENTITY_NAME = "microserviceGirInscriptionAdministrativeFormation";

    private final InscriptionAdministrativeFormationRepository inscriptionAdministrativeFormationRepository;

    private final InscriptionAdministrativeRepository inscriptionAdministrativeRepository;

    private final PaiementFraisRepository paiementFraisRepository;

    private final InscriptionAdministrativeFormationMapper inscriptionAdministrativeFormationMapper;

    private final InscriptionAdministrativeFormationSearchRepository inscriptionAdministrativeFormationSearchRepository;

    public InscriptionAdministrativeFormationServiceImpl(
            InscriptionAdministrativeFormationRepository inscriptionAdministrativeFormationRepository, InscriptionAdministrativeRepository inscriptionAdministrativeRepository, PaiementFraisRepository paiementFraisRepository,
            InscriptionAdministrativeFormationMapper inscriptionAdministrativeFormationMapper,
            InscriptionAdministrativeFormationSearchRepository inscriptionAdministrativeFormationSearchRepository
    ) {
        this.inscriptionAdministrativeFormationRepository = inscriptionAdministrativeFormationRepository;
        this.inscriptionAdministrativeRepository = inscriptionAdministrativeRepository;
        this.paiementFraisRepository = paiementFraisRepository;
        this.inscriptionAdministrativeFormationMapper = inscriptionAdministrativeFormationMapper;
        this.inscriptionAdministrativeFormationSearchRepository = inscriptionAdministrativeFormationSearchRepository;
    }

    @Override
    public InscriptionAdministrativeFormationDTO save(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO) {
        log.debug("Request to save InscriptionAdministrativeFormation : {}", inscriptionAdministrativeFormationDTO);
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation = inscriptionAdministrativeFormationMapper.toEntity(
            inscriptionAdministrativeFormationDTO
        );
        Instant currentDate = Instant.now();

        validateData( inscriptionAdministrativeFormationDTO);
        inscriptionAdministrativeFormationDTO.setDateChoixFormation(currentDate);
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

        validateData( inscriptionAdministrativeFormationDTO);
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

        validateData( inscriptionAdministrativeFormationDTO);
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

        PaiementFrais paiementFrais = paiementFraisRepository.paiementFraisIAF(id);
        if (paiementFrais.getEcheancePayeeYN()){
            throw new BadRequestAlertException("Vous ne pouvez pas supprimer un paiement déja valider par le paiement", ENTITY_NAME, "InscriptionFormationDejaPayer");
        }
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

    public void validateData(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO){


        if (Objects.equals(inscriptionAdministrativeFormationDTO.getFormation(),null)) {
            throw new BadRequestAlertException("Veuillez renseigné la formation à laquelle s'inscrit l'étudiant", "Formation", "FormationObligatoire");
        }
        if (Objects.equals(inscriptionAdministrativeFormationDTO.getInscriptionAdministrative().getEtudiant(),null)) {
            throw new BadRequestAlertException("Veuillez renseigné l'etudiant qui doit s'inscrire ", ENTITY_NAME, "EtudiantObligatoire");
        }
        if (Objects.equals(inscriptionAdministrativeFormationDTO.getInscriptionAdministrative().getAnneeAcademique(),null)) {
            throw new BadRequestAlertException("Veuillez renseigné l'année de l'inscription", ENTITY_NAME, "AnneeAcademiqueObligatoire");
        }
        if (Objects.equals(inscriptionAdministrativeFormationDTO.getInscriptionAdministrative().getTypeAdmission(),null)) {
            throw new BadRequestAlertException("Veuillez renseigné le type d'admission de l'étudiant", ENTITY_NAME, "TypeAdmissionObligatoire");
        }
        inscriptionAdministrativeFormationDTO.setInscriptionPrincipaleYN(TRUE);
        Long formationId = inscriptionAdministrativeFormationDTO.getFormation().getId() ;
        Long etudiantId = inscriptionAdministrativeFormationDTO.getInscriptionAdministrative().getEtudiant().getId();
        Long AnneeAcademiqueId = inscriptionAdministrativeFormationDTO.getInscriptionAdministrative().getAnneeAcademique().getId();
        Optional<InscriptionAdministrativeFormation> existingIAF= inscriptionAdministrativeFormationRepository.findByFormationIdAndInscriptionAdministrativeEtudiantIdAndInscriptionAdministrativeAnneeAcademiqueId(formationId,etudiantId,AnneeAcademiqueId);
        Page<InscriptionAdministrative> existingIA= inscriptionAdministrativeRepository.findByEtudiantIdAndAnneeAcademiqueId(etudiantId,AnneeAcademiqueId);
        Long count = existingIA.getTotalElements();

        if (existingIAF.isPresent()) {
            if (!Objects.equals(existingIAF.get().getId(), inscriptionAdministrativeFormationDTO.getId())) {
                throw new BadRequestAlertException("l'inscription de cet etudiant à cette formation est deja faite pour cet annee", "InscriptionAdministrativeFormation", "InscriptionAdministrativeFormationExiste");
            }
        }
//        if (existingIA.isPresent()) {
//            if (Objects.equals(existingIA.get().getId(), inscriptionAdministrativeFormationDTO.getInscriptionAdministrative().getId())  ) {
//               if ( !Objects.equals(existingIA.get().getFormation().getId(), inscriptionAdministrativeFormationDTO.getFormation().getId())) {
//                    inscriptionAdministrativeFormationDTO.setInscriptionPrincipaleYN(FALSE);
//                }
//
//            }
//        }

    }


}
