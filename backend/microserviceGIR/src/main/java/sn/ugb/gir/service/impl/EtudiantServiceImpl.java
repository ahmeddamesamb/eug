package sn.ugb.gir.service.impl;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.*;
import sn.ugb.gir.repository.EtudiantRepository;
import sn.ugb.gir.repository.InformationPersonnelleRepository;
import sn.ugb.gir.repository.PaiementFraisRepository;
import sn.ugb.gir.repository.ProcessusInscriptionAdministrativeRepository;
import sn.ugb.gir.repository.search.EtudiantSearchRepository;
import sn.ugb.gir.service.EtudiantService;
import sn.ugb.gir.service.LyceeService;
import sn.ugb.gir.service.RegionService;
import sn.ugb.gir.service.dto.*;
import sn.ugb.gir.service.mapper.*;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Etudiant}.
 */
@Service
@Transactional
public class EtudiantServiceImpl implements EtudiantService {


    private final Logger log = LoggerFactory.getLogger(EtudiantServiceImpl.class);

    private final EtudiantRepository etudiantRepository;

    private final EtudiantMapper etudiantMapper;

    private final EtudiantSearchRepository etudiantSearchRepository;

    @Autowired
    private LyceeService lyceeService;

    @Autowired
    private  RegionService regionService;

    @Autowired
    private PaiementFraisRepository paiementFraisRepository;

    @Autowired
    private PaiementFraisMapper paiementFraisMapper;

    @Autowired
    private InformationPersonnelleMapper informationPersonnelleMapper;

    @Autowired
    private InformationPersonnelleRepository informationPersonnelleRepository;

    @Autowired
    private FormationPriveeMapper formationPriveeMapper;

    @Autowired
    private ProcessusInscriptionAdministrativeRepository processusInscriptionAdministrativeRepository;

    @Autowired
    private ProcessusInscriptionAdministrativeMapper processusInscriptionAdministrativeMapper;

    private static final String CURRENT_YEAR = String.valueOf(LocalDate.now().getYear());


    public EtudiantServiceImpl(
        EtudiantRepository etudiantRepository,
        EtudiantMapper etudiantMapper,
        EtudiantSearchRepository etudiantSearchRepository
    ) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantMapper = etudiantMapper;
        this.etudiantSearchRepository = etudiantSearchRepository;
    }

    @Override
    public EtudiantDTO save(EtudiantDTO etudiantDTO) {
        log.debug("Request to save Etudiant : {}", etudiantDTO);

        validateData(etudiantDTO);

        if (etudiantDTO.getLycee() != null && etudiantDTO.getLycee().getId() == null) {
            // Save the Lycee entity first using LyceeService
            LyceeDTO lyceeDTO = etudiantDTO.getLycee();
            lyceeDTO = lyceeService.save(lyceeDTO);
            etudiantDTO.setLycee(lyceeDTO);
        }

        if (etudiantDTO.getRegion() != null && etudiantDTO.getRegion().getId() == null) {
            // Save the Region entity first using RegionService
            RegionDTO regionDTO = etudiantDTO.getRegion();
            regionDTO = regionService.save(regionDTO);
            etudiantDTO.setRegion(regionDTO);
        }

        generateTemporaryCode(etudiantDTO);

        Etudiant etudiant = etudiantMapper.toEntity(etudiantDTO);
        etudiant = etudiantRepository.save(etudiant);
        EtudiantDTO result = etudiantMapper.toDto(etudiant);
        etudiantSearchRepository.index(etudiant);
        return result;
    }

    @Override
    public EtudiantDTO update(EtudiantDTO etudiantDTO) {
        log.debug("Request to update Etudiant : {}", etudiantDTO);

        validateData(etudiantDTO);

        if (etudiantDTO.getLycee() != null && etudiantDTO.getLycee().getId() != null) {
            // Save the Lycee entity first using LyceeService
            LyceeDTO lyceeDTO = etudiantDTO.getLycee();
            lyceeDTO = lyceeService.update(lyceeDTO);
            etudiantDTO.setLycee(lyceeDTO);
        }

        if (etudiantDTO.getRegion() != null && etudiantDTO.getRegion().getId() != null) {
            // Save the Region entity first using RegionService
            RegionDTO regionDTO = etudiantDTO.getRegion();
            regionDTO = regionService.update(regionDTO);
            etudiantDTO.setRegion(regionDTO);
        }

        Etudiant etudiant = etudiantMapper.toEntity(etudiantDTO);
        etudiant = etudiantRepository.save(etudiant);
        EtudiantDTO result = etudiantMapper.toDto(etudiant);
        etudiantSearchRepository.index(etudiant);
        return result;
    }

    @Override
    public Optional<EtudiantDTO> partialUpdate(EtudiantDTO etudiantDTO) {
        log.debug("Request to partially update Etudiant : {}", etudiantDTO);

        validateData(etudiantDTO);

        return etudiantRepository
            .findById(etudiantDTO.getId())
            .map(existingEtudiant -> {
                etudiantMapper.partialUpdate(existingEtudiant, etudiantDTO);

                return existingEtudiant;
            })
            .map(etudiantRepository::save)
            .map(savedEtudiant -> {
                etudiantSearchRepository.index(savedEtudiant);
                return savedEtudiant;
            })
            .map(etudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EtudiantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Etudiants");
        return etudiantRepository.findAll(pageable).map(etudiantMapper::toDto);
    }

    /**
     *  Get all the etudiants where InformationPersonnelle is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EtudiantDTO> findAllWhereInformationPersonnelleIsNull() {
        log.debug("Request to get all etudiants where InformationPersonnelle is null");
        return StreamSupport
            .stream(etudiantRepository.findAll().spliterator(), false)
            .filter(etudiant -> etudiant.getInformationPersonnelle() == null)
            .map(etudiantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the etudiants where Baccalaureat is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EtudiantDTO> findAllWhereBaccalaureatIsNull() {
        log.debug("Request to get all etudiants where Baccalaureat is null");
        return StreamSupport
            .stream(etudiantRepository.findAll().spliterator(), false)
            .filter(etudiant -> etudiant.getBaccalaureat() == null)
            .map(etudiantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EtudiantDTO> findOne(Long id) {
        log.debug("Request to get Etudiant : {}", id);
        return etudiantRepository.findById(id).map(etudiantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etudiant : {}", id);
        etudiantRepository.deleteById(id);
        etudiantSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EtudiantDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Etudiants for query {}", query);
        return etudiantSearchRepository.search(query, pageable).map(etudiantMapper::toDto);
    }

    private void validateData(EtudiantDTO etudiantDTO) {
        if (etudiantDTO == null) {
            throw new BadRequestAlertException("Etudiant cannot be null", ENTITY_NAME, "nullEtudiant");
        }

        String codeEtu = etudiantDTO.getCodeEtu();
        String emailUGB = etudiantDTO.getEmailUGB();
        String numDocIdentite = etudiantDTO.getNumDocIdentite();
        String lieuNaissEtu = etudiantDTO.getLieuNaissEtu();
        String sexe = etudiantDTO.getSexe();
        LocalDate dateNaissEtu = etudiantDTO.getDateNaissEtu();

        if (dateNaissEtu == null || dateNaissEtu.isAfter(LocalDate.now())) {
            throw new BadRequestAlertException("La date de naissance ne doit pas être postérieure à la date du jour", ENTITY_NAME, "dateNaissanceInvalide");
        }

        if (isEmptyOrBlank(lieuNaissEtu)) {
            throw new BadRequestAlertException("Le lieu de naissance est obligatoire", ENTITY_NAME, "lieuNaissanceObligatoire");
        }

        if (isEmptyOrBlank(sexe)) {
            throw new BadRequestAlertException("Le sexe est obligatoire", ENTITY_NAME, "sexeObligatoire");
        }

        if (isEmptyOrBlank(numDocIdentite)) {
            throw new BadRequestAlertException("Le numero d'identite est déjà obligatoire", ENTITY_NAME, "numDocObligatoire");
        }


        Long id = etudiantDTO.getId();
        if (id == null) {
            // Save operation
            if (etudiantRepository.existsByCodeEtuIgnoreCase(codeEtu)) {
                throw new BadRequestAlertException("Cet identifiant est déjà utilisé", ENTITY_NAME, "codeEtuExistant");
            }

            if (etudiantRepository.existsByEmailUGBIgnoreCase(emailUGB)) {
                throw new BadRequestAlertException("Cet email est déjà utilisé", ENTITY_NAME, "emailUGBExistant");
            }

            if (etudiantRepository.existsByNumDocIdentiteIgnoreCase(numDocIdentite)) {
                throw new BadRequestAlertException("Ce numero d'identite est déjà utilisé", ENTITY_NAME, "numDocIdentiteExistant");
            }
        } else {
            // Update or PartialUpdate operation
            if (isEmptyOrBlank(codeEtu)) {
                throw new BadRequestAlertException("Le code étudiant est obligatoire", ENTITY_NAME, "codeEtuObligatoire");
            }

            if (isEmptyOrBlank(emailUGB)) {
                throw new BadRequestAlertException("L'email UGB est obligatoire", ENTITY_NAME, "emailUGBObligatoire");
            }
            Optional<Etudiant> existingEtudiant = etudiantRepository.findById(id);
            if (existingEtudiant.isEmpty()) {
                throw new BadRequestAlertException("Etudiant avec id " + id + " non trouvé", ENTITY_NAME, "etudiantNotFound");
            }

            Etudiant etudiant = existingEtudiant.get();
            if (!etudiant.getCodeEtu().equalsIgnoreCase(codeEtu) && etudiantRepository.existsByCodeEtuIgnoreCase(codeEtu)) {
                throw new BadRequestAlertException("Cet identifiant est déjà utilisé", ENTITY_NAME, "codeEtuExistant");
            }

            if (!etudiant.getNumDocIdentite().equalsIgnoreCase(numDocIdentite) && etudiantRepository.existsByNumDocIdentiteIgnoreCase(numDocIdentite)) {
                throw new BadRequestAlertException("Ce numero d'identite est déjà utilisé", ENTITY_NAME, "numDocIdentiteExistant");
            }
        }
    }

    private boolean isEmptyOrBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void generateTemporaryCode(EtudiantDTO etudiantDTO) {
        long rang = etudiantRepository.count() + 1;
        String codeTemporaire = (LocalDate.now().getYear() - 1) + "E" + rang;
        etudiantDTO.setCodeEtu(codeTemporaire);
    }

    @Override
    public DossierEtudiantDTO getEtudiantDetailsByCodeEtu(String codeEtudiant) {

        List<PaiementFrais> paiementFraisEntities = paiementFraisRepository.findByInscriptionAdministrativeFormationInscriptionAdministrativeEtudiantCodeEtu(codeEtudiant);

        if (paiementFraisEntities.isEmpty()) {
            throw new BadRequestAlertException("Ce code ne refere pas à un etudiant " + codeEtudiant, ENTITY_NAME, "codeNonTrouve");
        }

        List<PaiementFraisDTO> paiementFrais = paiementFraisEntities.stream()
            .map(paiementFraisMapper::toDto)
            .collect(Collectors.toList());

        List<ProcessusInscriptionAdministrative> processusInscriptionAdministrativeEntities = processusInscriptionAdministrativeRepository.findByInscriptionAdministrativeEtudiantCodeEtu(codeEtudiant);

        if (processusInscriptionAdministrativeEntities.isEmpty()) {
            throw new BadRequestAlertException("Aucun processus d'inscription administrative trouvé pour l'étudiant avec le code " + codeEtudiant, ENTITY_NAME, "aucunProcessusTrouve");
        }

        List<ProcessusInscriptionAdministrativeDTO> processusInscriptionAdministratives = processusInscriptionAdministrativeEntities.stream()
            .map(processusInscriptionAdministrativeMapper::toDto)
            .collect(Collectors.toList());

        // Recherche de la formation associée au premier paiement de frais trouvé
        Formation formation = paiementFraisEntities.stream()
            .map(pf -> pf.getInscriptionAdministrativeFormation().getFormation())
            .findFirst()
            .orElse(null);

        if (formation == null) {
            throw new BadRequestAlertException("Aucune formation trouvée pour l'étudiant avec le code " + codeEtudiant, ENTITY_NAME, "aucuneFormationTrouvee");
        }

        FormationPriveeDTO formationPriveeDTO = null;
        FormationPrivee formationPrivee = formation.getFormationPrivee();
        if (formationPrivee != null) {
            formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);
        }

        Optional<InformationPersonnelle> informationPersonnelleOptional = informationPersonnelleRepository.findByEtudiantCodeEtu(codeEtudiant);
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleOptional
            .map(informationPersonnelleMapper::toDto)
            .orElse(null);

        DossierEtudiantDTO dossierEtudiantDTO = new DossierEtudiantDTO();
        dossierEtudiantDTO.setInformationPersonnelle(informationPersonnelleDTO);
        dossierEtudiantDTO.setPaiementFrais(paiementFrais);
        dossierEtudiantDTO.setProcessusInscriptionAdministratives(processusInscriptionAdministratives);
        dossierEtudiantDTO.setFormationPrivee(formationPriveeDTO);

        return dossierEtudiantDTO;
    }

}

