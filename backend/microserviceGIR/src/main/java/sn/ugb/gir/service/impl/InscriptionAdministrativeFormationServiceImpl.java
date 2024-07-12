package sn.ugb.gir.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.domain.PaiementFrais;
import sn.ugb.gir.repository.InscriptionAdministrativeFormationRepository;
import sn.ugb.gir.repository.PaiementFraisRepository;
import sn.ugb.gir.repository.search.InscriptionAdministrativeFormationSearchRepository;
import sn.ugb.gir.service.InscriptionAdministrativeFormationService;
import sn.ugb.gir.service.dto.*;
import sn.ugb.gir.service.mapper.InscriptionAdministrativeFormationMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.service.mapper.InformationsDerniersInscriptionsMapper;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Service Implementation for managing {@link InscriptionAdministrativeFormation}.
 */
@Service
@Transactional
public class InscriptionAdministrativeFormationServiceImpl implements InscriptionAdministrativeFormationService {

    private final Logger log = LoggerFactory.getLogger(InscriptionAdministrativeFormationServiceImpl.class);

    private static final String ENTITY_NAME = "microserviceGirInscriptionAdministrativeFormation";

    private final InscriptionAdministrativeFormationRepository inscriptionAdministrativeFormationRepository;

    private final PaiementFraisRepository paiementFraisRepository;

    private final InscriptionAdministrativeFormationMapper inscriptionAdministrativeFormationMapper;

    private final InformationsDerniersInscriptionsMapper informationsDerniersInscriptionsMapper;


    private final InscriptionAdministrativeFormationSearchRepository inscriptionAdministrativeFormationSearchRepository;

    public InscriptionAdministrativeFormationServiceImpl(
            InscriptionAdministrativeFormationRepository inscriptionAdministrativeFormationRepository, PaiementFraisRepository paiementFraisRepository,
            InscriptionAdministrativeFormationMapper inscriptionAdministrativeFormationMapper, InformationsDerniersInscriptionsMapper informationsDerniersInscriptionsMapper,
            InscriptionAdministrativeFormationSearchRepository inscriptionAdministrativeFormationSearchRepository
    ) {
        this.inscriptionAdministrativeFormationRepository = inscriptionAdministrativeFormationRepository;
        this.paiementFraisRepository = paiementFraisRepository;
        this.inscriptionAdministrativeFormationMapper = inscriptionAdministrativeFormationMapper;
        this.informationsDerniersInscriptionsMapper = informationsDerniersInscriptionsMapper;
        this.inscriptionAdministrativeFormationSearchRepository = inscriptionAdministrativeFormationSearchRepository;
    }

    @Override
    public InscriptionAdministrativeFormationDTO save(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO) {
        log.debug("Request to save InscriptionAdministrativeFormation : {}", inscriptionAdministrativeFormationDTO);
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation = inscriptionAdministrativeFormationMapper.toEntity(
            inscriptionAdministrativeFormationDTO
        );
        Instant currentDate = Instant.now();

        Long etudiantId = inscriptionAdministrativeFormation.getInscriptionAdministrative().getEtudiant().getId();
        Long AnneeAcademiqueId = inscriptionAdministrativeFormation.getInscriptionAdministrative().getAnneeAcademique().getId();
        List<InscriptionAdministrativeFormation> IafExistants= inscriptionAdministrativeFormationRepository.findByInscriptionAdministrativeEtudiantIdAndInscriptionAdministrativeAnneeAcademiqueId(etudiantId,AnneeAcademiqueId);

        long nbIafEtudiant = IafExistants.size();
        if (nbIafEtudiant ==0) {
            inscriptionAdministrativeFormation.setInscriptionPrincipaleYN(TRUE);
        }else if (nbIafEtudiant ==1) {
            if (Objects.equals(IafExistants.get(0).getFormation(), inscriptionAdministrativeFormation.getFormation() )) {
                throw new BadRequestAlertException("l'inscription de cet etudiant à cette formation est deja faite pour cet annee", "InscriptionAdministrativeFormation", "InscriptionAdministrativeFormationExiste");
            } else {
                inscriptionAdministrativeFormation.setInscriptionPrincipaleYN(FALSE);
            }
        } else if (nbIafEtudiant ==2 ) {
            throw new BadRequestAlertException("l'étudiant possède deja deux inscriptions à deux formations distinctes", "InscriptionAdministrativeFormation", "InscriptionAdministrativeFormationTrippleImpossible");
        }
        validateData( inscriptionAdministrativeFormation);
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
        validateData( inscriptionAdministrativeFormation);
        validateDataUpdate(inscriptionAdministrativeFormation);
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
        Optional<InscriptionAdministrativeFormation> iaf = inscriptionAdministrativeFormationRepository.findById(id);
        if(iaf.isEmpty()){
            throw new BadRequestAlertException("L'element que vous tentez de supprimer n'existe", ENTITY_NAME, "InscriptionAdministrativeFormationNotExiste");
        }
        PaiementFrais paiementFrais = paiementFraisRepository.paiementFraisIAF(id);
        if (!Objects.equals(paiementFrais, null)){
            if (paiementFrais.getEcheancePayeeYN()){
                throw new BadRequestAlertException("Vous ne pouvez pas supprimer un paiement déja valider par le paiement", ENTITY_NAME, "InscriptionFormationDejaPayer");
            }
            else
                paiementFraisRepository.delete(paiementFrais);
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

    @Override
    public Page<InformationsDerniersInscriptionsDTO> findAllByDernierInscription(Pageable pageable) {
        log.debug("Request to get all the last InscriptionAdministrativeFormations");
        return inscriptionAdministrativeFormationRepository.findByLastInscription(pageable).map(informationsDerniersInscriptionsMapper::toDto);
    }

    public void validateData(InscriptionAdministrativeFormation inscriptionAdministrativeFormation){

        if (Objects.equals(inscriptionAdministrativeFormation.getFormation(),null)) {
            throw new BadRequestAlertException("Veuillez renseigné la formation à laquelle s'inscrit l'étudiant", "Formation", "FormationObligatoire");
        }
        if (Objects.equals(inscriptionAdministrativeFormation.getInscriptionAdministrative().getEtudiant(),null)) {
            throw new BadRequestAlertException("Veuillez renseigné l'etudiant qui doit s'inscrire ", ENTITY_NAME, "EtudiantObligatoire");
        }
        if (Objects.equals(inscriptionAdministrativeFormation.getInscriptionAdministrative().getAnneeAcademique(),null)) {
            throw new BadRequestAlertException("Veuillez renseigné l'année de l'inscription", ENTITY_NAME, "AnneeAcademiqueObligatoire");
        }
        if (Objects.equals(inscriptionAdministrativeFormation.getInscriptionAdministrative().getTypeAdmission(),null)) {
            throw new BadRequestAlertException("Veuillez renseigné le type d'admission de l'étudiant", ENTITY_NAME, "TypeAdmissionObligatoire");
        }
    }

    public void validateDataUpdate(InscriptionAdministrativeFormation inscriptionAdministrativeFormation){

        Long etudiantId = inscriptionAdministrativeFormation.getInscriptionAdministrative().getEtudiant().getId();
        Long AnneeAcademiqueId = inscriptionAdministrativeFormation.getInscriptionAdministrative().getAnneeAcademique().getId();
        Long formationId = inscriptionAdministrativeFormation.getFormation().getId();
        Optional<InscriptionAdministrativeFormation> IafExistant= inscriptionAdministrativeFormationRepository.findByFormationIdAndInscriptionAdministrativeEtudiantIdAndInscriptionAdministrativeAnneeAcademiqueId(formationId,etudiantId,AnneeAcademiqueId);

        if(IafExistant.isPresent() && !IafExistant.get().getId().equals(inscriptionAdministrativeFormation.getId())){
            throw new BadRequestAlertException("l'inscription de cet etudiant à cette formation est deja faite pour cet annee", "InscriptionAdministrativeFormation", "Cet inscription administrative à cette formation existe(est déja faite) pour cet étudiant");
        }
        if(IafExistant.isPresent() && IafExistant.get().getId().equals(inscriptionAdministrativeFormation.getId()) && !Objects.equals(IafExistant.get().getFormation().getNiveau().getTypeCycle().getId(),inscriptionAdministrativeFormation.getFormation().getNiveau().getTypeCycle().getId())){
            throw new BadRequestAlertException("Vous ne pouvez pas changer le Cycle, vous ne pouvez que modifier la fillière", "InscriptionAdministrativeFormation", "Le Cycle N'est Pas Modifiable, Modifier uniquement la fillière");
        }
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



