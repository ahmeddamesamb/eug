package sn.ugb.gir.service.impl;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Frais;
import sn.ugb.gir.domain.enumeration.Cycle;
import sn.ugb.gir.repository.FraisRepository;
import sn.ugb.gir.service.FraisService;
import sn.ugb.gir.service.dto.FraisDTO;
import sn.ugb.gir.service.mapper.FraisMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Frais}.
 */
@Service
@Transactional
public class FraisServiceImpl implements FraisService {

    private final Logger log = LoggerFactory.getLogger(FraisServiceImpl.class);

    private final FraisRepository fraisRepository;

    private final FraisMapper fraisMapper;

    public FraisServiceImpl(FraisRepository fraisRepository, FraisMapper fraisMapper) {
        this.fraisRepository = fraisRepository;
        this.fraisMapper = fraisMapper;
    }

    @Override
    public FraisDTO save(FraisDTO fraisDTO) {
        log.debug("Request to save Frais : {}", fraisDTO);
        LocalDate currentDate = LocalDate.now();

//        DOIT ON RENSEIGNER LE CHAMP ESTENAPPLICATION LORS DE LA CREATION ??????????????????????????????????????????????????????????????????????????????????
        if (fraisDTO.getId() != null) {
            throw new BadRequestAlertException("A new frais cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (fraisDTO.getDescriptionFrais().isEmpty() || fraisDTO.getDescriptionFrais().isBlank()) {
            throw new BadRequestAlertException("Veuillez renseigner la description du frais", ENTITY_NAME, "descriptionobligatoire");
        }
        if (fraisDTO.getCycle().describeConstable().isEmpty() ) {
            throw new BadRequestAlertException("Veuillez renseigner le cycle associé au frais", ENTITY_NAME, "cycleobligatoire");
        }
        if (fraisDTO.getValeurFrais().isNaN() ) {
            throw new BadRequestAlertException("Veuillez renseigner la valeur du frais", ENTITY_NAME, "valeurfraisobligatoire");
        }
        // A VALIDER !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (fraisDTO.getValeurFrais() < 5000 ) {
            throw new BadRequestAlertException("Veuillez renseigner une valeur de frais correcte", ENTITY_NAME, "valeurfraisinvalid");
        }
        if (fraisDTO.getDateApplication() == null ) {
            throw new BadRequestAlertException("Veuillez renseigner la date d'application du frais", ENTITY_NAME, "dateapplicationobligatoire");
        }
        if (fraisDTO.getTypeFrais().getId().describeConstable().isEmpty()) {
            throw new BadRequestAlertException("Veuillez renseigner le type de frais", ENTITY_NAME, "typefraisobligatoire");
        }

        if (fraisDTO.getDateApplication().isBefore(currentDate)) {
            throw new BadRequestAlertException("La date d'appliaction d'un nouveau frais ne peut pas etre dans le passe", ENTITY_NAME, "dateapplicationinvalide");
        }
        if (fraisDTO.getTypeFrais().getLibelleTypeFrais().equalsIgnoreCase("droit d'inscription") && (fraisDTO.getDia() == null || fraisDTO.getDip() == null)) {
            throw new BadRequestAlertException("les droits d'inscriptions requiert les repartitions en  dia et un dip", ENTITY_NAME, "dia_dip_null");
        }
        if (fraisDTO.getTypeFrais().getLibelleTypeFrais().equalsIgnoreCase("droit d'inscription du privée") && (fraisDTO.getDia() == null || fraisDTO.getDip() == null) || (fraisDTO.getDipPrivee() == null)) {
            throw new BadRequestAlertException("les droits d'inscriptions du privée requiert les repartitions en  dia et un dip et dipPrivée", ENTITY_NAME, "dia_dip_dipprivee_null");
        }

        fraisDTO.setDateFin(null);
        fraisRepository.updateIfEstEnApplicationIsOneAndCycleLike(fraisDTO.getCycle());
        fraisDTO.setEstEnApplicationYN(1);

        Frais frais = fraisMapper.toEntity(fraisDTO);
        frais = fraisRepository.save(frais);
        return fraisMapper.toDto(frais);
    }

    @Override
    public FraisDTO update(FraisDTO fraisDTO, Long id) {
        log.debug("Request to update Frais : {}", fraisDTO);

        if (fraisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fraisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fraisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (fraisDTO.getEstEnApplicationYN() == 1) {
            fraisDTO.setDateFin(null);
        }

        Frais frais = fraisMapper.toEntity(fraisDTO);
        frais = fraisRepository.save(frais);
        return fraisMapper.toDto(frais);
    }

    @Override
    public Optional<FraisDTO> partialUpdate(FraisDTO fraisDTO, Long id) {
        log.debug("Request to partially update Frais : {}", fraisDTO);

        if (fraisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fraisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fraisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (fraisDTO.getEstEnApplicationYN() == 1) {
            fraisDTO.setDateFin(null);
        }

        return fraisRepository
            .findById(fraisDTO.getId())
            .map(existingFrais -> {
                fraisMapper.partialUpdate(existingFrais, fraisDTO);

                return existingFrais;
            })
            .map(fraisRepository::save)
            .map(fraisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Frais");
        return fraisRepository.findAll(pageable).map(fraisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FraisDTO> findOne(Long id) {
        log.debug("Request to get Frais : {}", id);
        return fraisRepository.findById(id).map(fraisMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Frais : {}", id);
        fraisRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraisDTO> findAllFraisByCycle(Pageable pageable, Cycle cycle) {
        log.debug("Request to get all Frais for a cycle");
        return fraisRepository.findFraisByCycle(pageable,cycle).map(fraisMapper::toDto);
    }


}
