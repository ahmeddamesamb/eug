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
import sn.ugb.gir.domain.Cycle;
import sn.ugb.gir.repository.FraisRepository;
import sn.ugb.gir.repository.search.FraisSearchRepository;
import sn.ugb.gir.service.FraisService;
import sn.ugb.gir.service.dto.CycleDTO;
import sn.ugb.gir.service.dto.FraisDTO;
import sn.ugb.gir.service.mapper.CycleMapper;
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
    private final CycleMapper cycleMapper;

    private final FraisSearchRepository fraisSearchRepository;

    public FraisServiceImpl(FraisRepository fraisRepository, FraisMapper fraisMapper, CycleMapper cycleMapper, FraisSearchRepository fraisSearchRepository) {
        this.fraisRepository = fraisRepository;
        this.fraisMapper = fraisMapper;
        this.cycleMapper = cycleMapper;
        this.fraisSearchRepository = fraisSearchRepository;
    }

    @Override
    public FraisDTO save(FraisDTO fraisDTO) {
        log.debug("Request to save Frais : {}", fraisDTO);
        LocalDate currentDate = LocalDate.now();
        CycleDTO cycleDTO = fraisDTO.getTypeCycle();
        Cycle cycle = cycleMapper.toEntity(cycleDTO);

        if (fraisDTO.getDateApplication().isBefore(currentDate)) {
            throw new BadRequestAlertException("La date d'appliaction d'un nouveau frais ne peut pas etre dans le passe", ENTITY_NAME, "dateDapplicationInvalide");
        }
        fraisRepository.updateIfEstEnApplicationIsOneAndCycleAndFraisPourAssimileYNLike(cycle,fraisDTO.getFraisPourAssimileYN());
        fraisDTO.setEstEnApplicationYN(true);
        validateData(fraisDTO);

        Frais frais = fraisMapper.toEntity(fraisDTO);
        frais = fraisRepository.save(frais);
        FraisDTO result = fraisMapper.toDto(frais);
        fraisSearchRepository.index(frais);
        return result;
    }

    @Override
    public FraisDTO update(FraisDTO fraisDTO) {
        log.debug("Request to update Frais : {}", fraisDTO);

        validateData(fraisDTO);
        Frais frais = fraisMapper.toEntity(fraisDTO);
        frais = fraisRepository.save(frais);
        FraisDTO result = fraisMapper.toDto(frais);
        fraisSearchRepository.index(frais);
        return result;
    }

    @Override
    public Optional<FraisDTO> partialUpdate(FraisDTO fraisDTO) {
        log.debug("Request to partially update Frais : {}", fraisDTO);

        validateData(fraisDTO);
        return fraisRepository
            .findById(fraisDTO.getId())
            .map(existingFrais -> {
                fraisMapper.partialUpdate(existingFrais, fraisDTO);

                return existingFrais;
            })
            .map(fraisRepository::save)
            .map(savedFrais -> {
                fraisSearchRepository.index(savedFrais);
                return savedFrais;
            })
            .map(fraisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Frais");
        return fraisRepository.findAll(pageable).map(fraisMapper::toDto);
    }

    public Page<FraisDTO> findAllWithEagerRelationships(Pageable pageable) {
        return fraisRepository.findAllWithEagerRelationships(pageable).map(fraisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FraisDTO> findOne(Long id) {
        log.debug("Request to get Frais : {}", id);
        return fraisRepository.findOneWithEagerRelationships(id).map(fraisMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Frais : {}", id);
        fraisRepository.deleteById(id);
        fraisSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraisDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Frais for query {}", query);
        return fraisSearchRepository.search(query, pageable).map(fraisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraisDTO> findAllFraisByCycleId(Pageable pageable, Long cycleID) {
        log.debug("Request to get all Frais for a cycle");
        return fraisRepository.findByTypeCycleId(pageable,cycleID).map(fraisMapper::toDto);
    }

    public Page<FraisDTO> findAllFraisByUniversiteId(Pageable pageable, Long universiteId){
        log.debug("Request to get all Frais for an universite");
        return fraisRepository.findAllByUniversitesId(pageable,universiteId).map(fraisMapper::toDto);
    }

    public Page<FraisDTO> findAllFraisByMinistereId(Pageable pageable, Long ministereId){
        log.debug("Request to get all Frais for an universite");
        return fraisRepository.findAllByUniversitesMinistereId(pageable,ministereId).map(fraisMapper::toDto);
    }

    private void validateData (FraisDTO fraisDTO) {

        //        DOIT ON RENSEIGNER LE CHAMP ESTENAPPLICATION LORS DE LA CREATION ??????????????????????????????????????????????????????????????????????????????????

        if (fraisDTO.getDescriptionFrais().isEmpty() || fraisDTO.getDescriptionFrais().isBlank()) {
            throw new BadRequestAlertException("Veuillez renseigner la description du frais", ENTITY_NAME, "descriptionObligatoire");
        }

        if (Objects.equals(fraisDTO.getTypeCycle(), null)) {
            throw new BadRequestAlertException("Veuillez renseigner le cycle associé au frais", ENTITY_NAME, "cycleObligatoire");
        }
        if (fraisDTO.getValeurFrais().isNaN() || Objects.equals(fraisDTO.getValeurFrais(), null) || fraisDTO.getValeurFrais() <= 0) {
            throw new BadRequestAlertException("Veuillez renseigner une valeur correcte du frais", ENTITY_NAME, "InvalidvaleurFrais");
        }
        if (Objects.equals(fraisDTO.getDateApplication(), null)) {
            throw new BadRequestAlertException("Veuillez renseigner la date d'application du frais", ENTITY_NAME, "dateDApplicationObligatoire");
        }
        if (Objects.equals(fraisDTO.getTypeFrais(), null)) {
            throw new BadRequestAlertException("Veuillez renseigner le type de frais", ENTITY_NAME, "typeFraisObligatoire");
        }
        if (Objects.equals(fraisDTO.getFraisPourAssimileYN(), null)) {
            throw new BadRequestAlertException("Veuillez renseigner si c'est un frais pour assimiler ou non", ENTITY_NAME, "fraisPourAssimileYNObligatoire");
        }
        if (Objects.equals(fraisDTO.getEstEnApplicationYN(), null)) {
            throw new BadRequestAlertException("Veuillez renseigner le cycle est en application ou non", ENTITY_NAME, "estEnApplicationYNObligatoire");
        }

        if (fraisDTO.getTypeFrais().getLibelleTypeFrais().equalsIgnoreCase("droit d'inscription") && (fraisDTO.getDia() == null || fraisDTO.getDip() == null)) {
            throw new BadRequestAlertException("les droits d'inscriptions requiert les repartitions en  dia et un dip", ENTITY_NAME, "dia_dip_null");
        }
        if (fraisDTO.getTypeFrais().getLibelleTypeFrais().equalsIgnoreCase("droit d'inscription du privée") && ((fraisDTO.getDia() == null || fraisDTO.getDip() == null) || (fraisDTO.getFraisPrivee() == null))) {
            throw new BadRequestAlertException("les droits d'inscriptions du privée requiert les repartitions en  dia et un dip et dipPrivée", ENTITY_NAME, "dia_dip_dipprivee_null");
        }
        if (fraisDTO.getEstEnApplicationYN()){
            fraisDTO.setDateFin(null);
        }
        //POUR LA DATE PRECEDANT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }
}
