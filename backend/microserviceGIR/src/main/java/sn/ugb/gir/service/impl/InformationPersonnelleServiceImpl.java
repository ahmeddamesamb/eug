package sn.ugb.gir.service.impl;

import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.repository.InformationPersonnelleRepository;
import sn.ugb.gir.repository.search.InformationPersonnelleSearchRepository;
import sn.ugb.gir.service.*;
import sn.ugb.gir.service.dto.*;

import sn.ugb.gir.service.mapper.InformationPersonnelleMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.InformationPersonnelle}.
 */
@Service
@Transactional
public class InformationPersonnelleServiceImpl implements InformationPersonnelleService {

    private final Logger log = LoggerFactory.getLogger(InformationPersonnelleServiceImpl.class);

    private final InformationPersonnelleRepository informationPersonnelleRepository;

    private final InformationPersonnelleMapper informationPersonnelleMapper;

    private final InformationPersonnelleSearchRepository informationPersonnelleSearchRepository;

    @Autowired
    private TypeHandicapService typeHandicapService;
    @Autowired
    private TypeBourseService typeBourseService;
    @Autowired
    private EtudiantService etudiantService;
    @Autowired
    BaccalaureatService baccalauriatService;

    public InformationPersonnelleServiceImpl(
        InformationPersonnelleRepository informationPersonnelleRepository,
        InformationPersonnelleMapper informationPersonnelleMapper,
        InformationPersonnelleSearchRepository informationPersonnelleSearchRepository
    ) {
        this.informationPersonnelleRepository = informationPersonnelleRepository;
        this.informationPersonnelleMapper = informationPersonnelleMapper;
        this.informationPersonnelleSearchRepository = informationPersonnelleSearchRepository;
    }

    @Override
    @Transactional
    public EtudiantBaccalauriatDTO saveEtudiantBaccalauriat(EtudiantBaccalauriatDTO etudiantBaccalauriatDTO) {
        log.debug("Request to save InformationPersonnelle : {}", etudiantBaccalauriatDTO);
        //InformationPersonnelleDTO informationPersonnelleDTO = etudiantBaccalauriatDTO.getInformationPersonnelle();

        if (etudiantBaccalauriatDTO.getInformationPersonnelle() != null && etudiantBaccalauriatDTO.getInformationPersonnelle().getId() == null) {
            // Save the Etudiant entity first using EtudiantService
            InformationPersonnelleDTO informationPersonnelleDTO = etudiantBaccalauriatDTO.getInformationPersonnelle();
            informationPersonnelleDTO = this.save(informationPersonnelleDTO);
            etudiantBaccalauriatDTO.setInformationPersonnelle(informationPersonnelleDTO);
        }
        if (etudiantBaccalauriatDTO.getBaccalaureatDTO() != null && etudiantBaccalauriatDTO.getBaccalaureatDTO().getId() == null) {
            // Save the Etudiant entity first using EtudiantService
            BaccalaureatDTO baccalaureatDTO = etudiantBaccalauriatDTO.getBaccalaureatDTO();
            Long idEtudiant = etudiantBaccalauriatDTO.getInformationPersonnelle().getEtudiant().getId();
            log.debug("Request to save InformationPersonnelle Id    ETUDIANT @@@@@@@@@@@@@@@@@@@@@@@@@@@@ : {}", idEtudiant);

            if (baccalaureatDTO.getEtudiant() == null) {
                baccalaureatDTO.setEtudiant(new EtudiantDTO());
            }
            if(idEtudiant!=null){
                baccalaureatDTO.getEtudiant().setId(idEtudiant);
            }
            else
                throw new BadRequestAlertException(" ETUDIANT @@@@@@@@@@@@@@@@@@@@@@@@@@@@", ENTITY_NAME, "idEtudiantBaccalauriatNull");

            baccalaureatDTO = baccalauriatService.save(baccalaureatDTO);
            etudiantBaccalauriatDTO.setBaccalaureatDTO(baccalaureatDTO);
        }
        return etudiantBaccalauriatDTO;
    }

    @Override
    @Transactional
    public InformationPersonnelleDTO save(InformationPersonnelleDTO informationPersonnelleDTO) {
        log.debug("Request to save InformationPersonnelle : {}", informationPersonnelleDTO);

        validateData(informationPersonnelleDTO,true);

        if (informationPersonnelleDTO.getEtudiant() != null && informationPersonnelleDTO.getEtudiant().getId() == null) {
            // Save the Etudiant entity first using EtudiantService
            EtudiantDTO etudiantDTO = informationPersonnelleDTO.getEtudiant();
            etudiantDTO = etudiantService.save(etudiantDTO);
            informationPersonnelleDTO.setEtudiant(etudiantDTO);
        }

        if (informationPersonnelleDTO.getTypeHandicap() != null && informationPersonnelleDTO.getTypeHandicap().getId() == null) {
            // Save the TypeHandicap entity first using TypeHandicapService
            TypeHandicapDTO typeHandicapDTO = informationPersonnelleDTO.getTypeHandicap();
            typeHandicapDTO = typeHandicapService.save(typeHandicapDTO);
            informationPersonnelleDTO.setTypeHandicap(typeHandicapDTO);
        }

        if (informationPersonnelleDTO.getTypeBourse() != null && informationPersonnelleDTO.getTypeBourse().getId() == null) {
            // Save the TypeBourse entity first using TypeBourseService
            TypeBourseDTO typeBourseDTO = informationPersonnelleDTO.getTypeBourse();
            typeBourseDTO = typeBourseService.save(typeBourseDTO);
            informationPersonnelleDTO.setTypeBourse(typeBourseDTO);
        }

        InformationPersonnelle informationPersonnelle = informationPersonnelleMapper.toEntity(informationPersonnelleDTO);
        informationPersonnelle = informationPersonnelleRepository.save(informationPersonnelle);
        InformationPersonnelleDTO result = informationPersonnelleMapper.toDto(informationPersonnelle);
        informationPersonnelleSearchRepository.index(informationPersonnelle);
        return result;
    }

    @Override
    @Transactional
    public InformationPersonnelleDTO update(InformationPersonnelleDTO informationPersonnelleDTO) {
        log.debug("Request to update InformationPersonnelle : {}", informationPersonnelleDTO);

        validateData(informationPersonnelleDTO,true);

        if (informationPersonnelleDTO.getEtudiant() != null && informationPersonnelleDTO.getEtudiant().getId() != null) {
            // Save the Etudiant entity first using EtudiantService
            EtudiantDTO etudiantDTO = informationPersonnelleDTO.getEtudiant();
            etudiantDTO = etudiantService.update(etudiantDTO);
            informationPersonnelleDTO.setEtudiant(etudiantDTO);
        }

        if (informationPersonnelleDTO.getTypeHandicap() != null && informationPersonnelleDTO.getTypeHandicap().getId() != null) {
            // Save the TypeHandicap entity first using TypeHandicapService
            TypeHandicapDTO typeHandicapDTO = informationPersonnelleDTO.getTypeHandicap();
            typeHandicapDTO = typeHandicapService.update(typeHandicapDTO);
            informationPersonnelleDTO.setTypeHandicap(typeHandicapDTO);
        }

        if (informationPersonnelleDTO.getTypeBourse() != null && informationPersonnelleDTO.getTypeBourse().getId() != null) {
            // Save the TypeBourse entity first using TypeBourseService
            TypeBourseDTO typeBourseDTO = informationPersonnelleDTO.getTypeBourse();
            typeBourseDTO = typeBourseService.update(typeBourseDTO);
            informationPersonnelleDTO.setTypeBourse(typeBourseDTO);
        }

        InformationPersonnelle informationPersonnelle = informationPersonnelleMapper.toEntity(informationPersonnelleDTO);
        informationPersonnelle = informationPersonnelleRepository.save(informationPersonnelle);
        InformationPersonnelleDTO result = informationPersonnelleMapper.toDto(informationPersonnelle);
        informationPersonnelleSearchRepository.index(informationPersonnelle);
        return result;
    }

    @Override
    public Optional<InformationPersonnelleDTO> partialUpdate(InformationPersonnelleDTO informationPersonnelleDTO) {
        log.debug("Request to partially update InformationPersonnelle : {}", informationPersonnelleDTO);

        return informationPersonnelleRepository
            .findById(informationPersonnelleDTO.getId())
            .map(existingInformationPersonnelle -> {
                informationPersonnelleMapper.partialUpdate(existingInformationPersonnelle, informationPersonnelleDTO);

                return existingInformationPersonnelle;
            })
            .map(informationPersonnelleRepository::save)
            .map(savedInformationPersonnelle -> {
                informationPersonnelleSearchRepository.index(savedInformationPersonnelle);
                return savedInformationPersonnelle;
            })
            .map(informationPersonnelleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InformationPersonnelleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InformationPersonnelles");
        return informationPersonnelleRepository.findAll(pageable).map(informationPersonnelleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InformationPersonnelleDTO> findOne(Long id) {
        log.debug("Request to get InformationPersonnelle : {}", id);
        return informationPersonnelleRepository.findById(id).map(informationPersonnelleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InformationPersonnelleDTO> findOneByCodeEtudiant(String code) {
        log.debug("Request to get InformationPersonnelle : {}", code);
        return informationPersonnelleRepository.findByEtudiantCodeEtu(code).map(informationPersonnelleMapper::toDto);
    }


    @Override
    public void delete(Long id) {
        log.debug("Request to delete InformationPersonnelle : {}", id);
        informationPersonnelleRepository.deleteById(id);
        informationPersonnelleSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InformationPersonnelleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InformationPersonnelles for query {}", query);
        return informationPersonnelleSearchRepository.search(query, pageable).map(informationPersonnelleMapper::toDto);
    }
    private void validateData(InformationPersonnelleDTO informationPersonnelleDTO, boolean isAdmin) {
        if (informationPersonnelleDTO == null) {
            throw new BadRequestAlertException("InformationPersonnelle cannot be null", ENTITY_NAME, "nullInformationPersonnelle");
        }

        String situationMatrimoniale = informationPersonnelleDTO.getStatutMarital();
        String telephone = informationPersonnelleDTO.getTelEtu();
        String telephoneParent = informationPersonnelleDTO.getTelParent();
        String emailPersonnel = informationPersonnelleDTO.getEmailEtu();
        String prenomParent = informationPersonnelleDTO.getPrenomParent();
        String nomParent = informationPersonnelleDTO.getNomParent();
        String adresseParent = informationPersonnelleDTO.getAdresseParent();

        //String emailPattern = "^[a-z]+\\.[a-z]+\\.[0-9]+@ugb\\.edu\\.sn$";
        String telephoePattern = "^\\+[1-9]\\d{1,14}$";


        if (isEmptyOrBlank(situationMatrimoniale)) {
            throw new BadRequestAlertException("La situation matrimoniale est obligatoire", ENTITY_NAME, "situationMatrimonialeObligatoire");
        }

        if (isEmptyOrBlank(telephone)) {
            throw new BadRequestAlertException("Le téléphone est obligatoire", ENTITY_NAME, "telephoneObligatoire");
        }
        if (!Pattern.matches(telephoePattern, telephone) ) {
            throw new BadRequestAlertException("Format numero de téléphone de l'etudiant est invalide", ENTITY_NAME, "invalideTelEtu");
        }

        if (!isEmptyOrBlank(telephoneParent) && !Pattern.matches(telephoePattern, telephone) ) {
            throw new BadRequestAlertException("Format numero de téléphone du parent de l'etudiant est invalide", ENTITY_NAME, "invalideTelEtu");
        }

        if (isEmptyOrBlank(emailPersonnel)) {
            throw new BadRequestAlertException("L'email personnel est obligatoire", ENTITY_NAME, "emailPersonnelObligatoire");
        }

        if (isEmptyOrBlank(prenomParent)) {
            throw new BadRequestAlertException("Le prénom du parent est obligatoire", ENTITY_NAME, "prenomParentObligatoire");
        }

        if (isEmptyOrBlank(nomParent)) {
            throw new BadRequestAlertException("Le nom du parent est obligatoire", ENTITY_NAME, "nomParentObligatoire");
        }

        if (isEmptyOrBlank(adresseParent)) {
            throw new BadRequestAlertException("L'adresse du parent est obligatoire", ENTITY_NAME, "adresseParentObligatoire");
        }

        Long id = informationPersonnelleDTO.getId();
        if (id != null) {
            Optional<InformationPersonnelle> existingInformationPersonnelle = informationPersonnelleRepository.findById(id);
            if (existingInformationPersonnelle.isPresent()) {
                InformationPersonnelle informationPersonnelle = existingInformationPersonnelle.get();

                if (!isAdmin) {
                    if (!informationPersonnelle.getRegime().equals(informationPersonnelleDTO.getRegime())) {
                        throw new BadRequestAlertException("L'identifiant ne peut être modifié que par l'administrateur", ENTITY_NAME, "identifiantNonModifiable");
                    }
                    if (!informationPersonnelle.getNomEtu().equals(informationPersonnelleDTO.getNomEtu())) {
                        throw new BadRequestAlertException("Le nom ne peut être modifié que par l'administrateur", ENTITY_NAME, "nomNonModifiable");
                    }
                    if (!informationPersonnelle.getPrenomEtu().equals(informationPersonnelleDTO.getPrenomEtu())) {
                        throw new BadRequestAlertException("Le prénom ne peut être modifié que par l'administrateur", ENTITY_NAME, "prenomNonModifiable");
                    }
                    if (!informationPersonnelle.getProfession().equals(informationPersonnelleDTO.getProfession())) {
                        throw new BadRequestAlertException("L'INE ne peut être modifié que par l'administrateur", ENTITY_NAME, "ineNonModifiable");
                    }
                    if (!informationPersonnelle.getAdresseEtu().equals(informationPersonnelleDTO.getAdresseEtu())) {
                        throw new BadRequestAlertException("La date de naissance ne peut être modifiée que par l'administrateur", ENTITY_NAME, "dateNaissanceNonModifiable");
                    }
                    if (!informationPersonnelle.getTelEtu().equals(informationPersonnelleDTO.getTelEtu())) {
                        throw new BadRequestAlertException("Le lieu de naissance ne peut être modifié que par l'administrateur", ENTITY_NAME, "lieuNaissanceNonModifiable");
                    }
                    if (!informationPersonnelle.getAdresseParent().equals(informationPersonnelleDTO.getAdresseParent())) {
                        throw new BadRequestAlertException("L'adresse parent ne peut être modifié que par l'administrateur", ENTITY_NAME, "adresseParentNonModifiable");
                    }
                }
            } else {
                throw new BadRequestAlertException("InformationPersonnelle avec id " + id + " non trouvé", ENTITY_NAME, "informationPersonnelleNotFound");
            }
        }
    }

    private boolean isEmptyOrBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
