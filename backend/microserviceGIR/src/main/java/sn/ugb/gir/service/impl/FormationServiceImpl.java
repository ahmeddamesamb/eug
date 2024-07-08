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
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.repository.FormationRepository;
import sn.ugb.gir.repository.search.FormationSearchRepository;
import sn.ugb.gir.service.FormationService;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.mapper.FormationMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Formation}.
 */
@Service
@Transactional
public class FormationServiceImpl implements FormationService {

    private final Logger log = LoggerFactory.getLogger(FormationServiceImpl.class);

    private static final String ENTITY_NAME = "Formation";

    private final FormationRepository formationRepository;

    private final FormationMapper formationMapper;

    private final FormationSearchRepository formationSearchRepository;

    public FormationServiceImpl(
        FormationRepository formationRepository,
        FormationMapper formationMapper,
        FormationSearchRepository formationSearchRepository
    ) {
        this.formationRepository = formationRepository;
        this.formationMapper = formationMapper;
        this.formationSearchRepository = formationSearchRepository;
    }

    @Override
    public FormationDTO save(FormationDTO formationDTO) {
        log.debug("Request to save Formation : {}", formationDTO);
        validateFormation(formationDTO);
        if (formationDTO.getId() == null) {
            formationDTO.setActifYN(true);
        }
        return storeFormation(formationDTO);
    }


    @Override
    public FormationDTO update(FormationDTO formationDTO) {
        log.debug("Request to update Formation : {}", formationDTO);
        validateFormation(formationDTO);
        return storeFormation(formationDTO);
    }

    @Override
    public Optional<FormationDTO> partialUpdate(FormationDTO formationDTO) {
        log.debug("Request to partially update Formation : {}", formationDTO);
        validateFormation(formationDTO);
        return formationRepository
            .findById(formationDTO.getId())
            .map(existingFormation -> {
                formationMapper.partialUpdate(existingFormation, formationDTO);
                return existingFormation;
            })
            .map(formationRepository::save)
            .map(formationMapper::toDto);
    }

    private void validateFormation(FormationDTO formationDTO) {
        Optional<Formation> existingFormation = formationRepository.findByNiveauIdAndSpecialiteId(formationDTO.getNiveau().getId(), formationDTO.getSpecialite().getId());

        if(formationDTO.getNiveau()==null) {
            throw new BadRequestAlertException("Le niveau du ministere ne dois pas etre null", ENTITY_NAME, "NiveauNotNull");
        }
        if(formationDTO.getSpecialite()==null) {
            throw new BadRequestAlertException("La spécialité du ministere ne dois pas etre null", ENTITY_NAME, "SpecialiteNotNull");
        }
        if (existingFormation.isPresent()) {
            throw new BadRequestAlertException("Cette Formation existe deja avec le meme Niveau et la meme Specialite", ENTITY_NAME, "FormationDuplicate");
        }
    }

    private FormationDTO storeFormation(FormationDTO formationDTO) {
        Formation formation = formationMapper.toEntity(formationDTO);
        formation = formationRepository.save(formation);
        return formationMapper.toDto(formation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Formations");
        return formationRepository.findAll(pageable).map(formationMapper::toDto);
    }

    /**
     *  Get all the formations where FormationPrivee is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FormationDTO> findAllWhereFormationPriveeIsNull() {
        log.debug("Request to get all formations where FormationPrivee is null");
        return StreamSupport
            .stream(formationRepository.findAll().spliterator(), false)
            .filter(formation -> formation.getFormationPrivee() == null)
            .map(formationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormationDTO> findOne(Long id) {
        log.debug("Request to get Formation : {}", id);
        return formationRepository.findById(id).map(formationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Formation : {}", id);
        formationRepository.deleteById(id);
        formationSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Formations for query {}", query);
        return formationSearchRepository.search(query, pageable).map(formationMapper::toDto);
    }

    @Override
    public Page<FormationDTO> findAllFormationByMention(Long mentionId, Pageable pageable) {
        return formationRepository.findBySpecialiteMentionId(mentionId, pageable)
            .map(formationMapper::toDto);
    }

    @Override
    public Page<FormationDTO> findAllFormationByDomaine(Long domaineId, Pageable pageable) {
        return formationRepository.findBySpecialiteMentionDomaineId(domaineId, pageable)
            .map(formationMapper::toDto);
    }

    @Override
    public Page<FormationDTO> findAllFormationByUfr(Long ufrId, Pageable pageable) {
        return formationRepository.findBySpecialiteMentionDomaineUfrsId(ufrId, pageable)
            .map(formationMapper::toDto);
    }

    @Override
    public Page<FormationDTO> findAllFormationByCycle(String cycle, Pageable pageable) {
        return formationRepository.findByNiveauTypeCycleLibelleCycleIgnoreCase(cycle, pageable)
            .map(formationMapper::toDto);
    }

    @Override
    public Page<FormationDTO> findAllFormationByUniversite(Long universiteId, Pageable pageable) {
        return formationRepository.findBySpecialiteMentionDomaineUfrsUniversiteId(universiteId, pageable)
            .map(formationMapper::toDto);
    }

    @Override
    public Page<FormationDTO> findAllFormationByMinistere(Long ministereId, Pageable pageable) {
        return formationRepository.findBySpecialiteMentionDomaineUfrsUniversiteMinistereId(ministereId, pageable)
            .map(formationMapper::toDto);
    }

    @Override
    public Page<FormationDTO> findAllFormationPubliqueByUniversite(Long universiteId, Pageable pageable) {
        return formationRepository.findBySpecialiteMentionDomaineUfrsUniversiteIdAndTypeFormationLibelleTypeFormation(universiteId, "PUBLIC", pageable)
            .map(formationMapper::toDto);
    }

    @Override
    public Page<FormationDTO> findAllFormationPriveeByUniversite(Long universiteId, Pageable pageable) {
        return formationRepository.findBySpecialiteMentionDomaineUfrsUniversiteIdAndTypeFormationLibelleTypeFormation(universiteId, "PRIVEE", pageable)
            .map(formationMapper::toDto);
    }

    @Override
    public Page<FormationDTO> findAllFormationPubliqueByMinistere(Long ministereId, Pageable pageable) {
        return formationRepository.findBySpecialiteMentionDomaineUfrsUniversiteMinistereIdAndTypeFormationLibelleTypeFormation(ministereId, "PUBLIC", pageable)
            .map(formationMapper::toDto);
    }

    @Override
    public Page<FormationDTO> findAllFormationPriveeByMinistere(Long ministereId, Pageable pageable) {
        return formationRepository.findBySpecialiteMentionDomaineUfrsUniversiteMinistereIdAndTypeFormationLibelleTypeFormation(ministereId, "PRIVEE", pageable)
            .map(formationMapper::toDto);
    }

    @Override
    public FormationDTO setActifYNFormation(Long id, Boolean actifYN) {
        Formation formation = formationRepository.findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Formation introuvable.", ENTITY_NAME, "formationNotFound"));
        formation.setActifYN(actifYN);
        formation = formationRepository.save(formation);
        return formationMapper.toDto(formation);
    }

}
