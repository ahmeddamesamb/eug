package sn.ugb.gir.service.impl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.FormationAutorisee;
import sn.ugb.gir.repository.FormationAutoriseeRepository;
import sn.ugb.gir.repository.FormationRepository;
import sn.ugb.gir.repository.search.FormationAutoriseeSearchRepository;
import sn.ugb.gir.service.FormationAutoriseeService;
import sn.ugb.gir.service.dto.FormationAutoriseeDTO;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.mapper.FormationAutoriseeMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.FormationAutorisee}.
 */
@Service
@Transactional
public class FormationAutoriseeServiceImpl implements FormationAutoriseeService {

    private final Logger log = LoggerFactory.getLogger(FormationAutoriseeServiceImpl.class);

    private final FormationAutoriseeRepository formationAutoriseeRepository;

    private final FormationAutoriseeMapper formationAutoriseeMapper;

    private final FormationAutoriseeSearchRepository formationAutoriseeSearchRepository;

    @Autowired
    private FormationRepository formationRepository;

    private static final String ENTITY_NAME = "FormationAutorisee";


    public FormationAutoriseeServiceImpl(
        FormationAutoriseeRepository formationAutoriseeRepository,
        FormationAutoriseeMapper formationAutoriseeMapper,
        FormationAutoriseeSearchRepository formationAutoriseeSearchRepository
    ) {
        this.formationAutoriseeRepository = formationAutoriseeRepository;
        this.formationAutoriseeMapper = formationAutoriseeMapper;
        this.formationAutoriseeSearchRepository = formationAutoriseeSearchRepository;
    }

    @Override
    public FormationAutoriseeDTO save(FormationAutoriseeDTO formationAutoriseeDTO) {
        log.debug("Request to save FormationAutorisee : {}", formationAutoriseeDTO);
        FormationAutorisee formationAutorisee = formationAutoriseeMapper.toEntity(formationAutoriseeDTO);
        formationAutorisee = formationAutoriseeRepository.save(formationAutorisee);
        FormationAutoriseeDTO result = formationAutoriseeMapper.toDto(formationAutorisee);
        formationAutoriseeSearchRepository.index(formationAutorisee);
        return result;
    }

    @Override
    public FormationAutoriseeDTO update(FormationAutoriseeDTO formationAutoriseeDTO) {
        log.debug("Request to update FormationAutorisee : {}", formationAutoriseeDTO);
        FormationAutorisee formationAutorisee = formationAutoriseeMapper.toEntity(formationAutoriseeDTO);
        formationAutorisee = formationAutoriseeRepository.save(formationAutorisee);
        FormationAutoriseeDTO result = formationAutoriseeMapper.toDto(formationAutorisee);
        formationAutoriseeSearchRepository.index(formationAutorisee);
        return result;
    }

    @Override
    public Optional<FormationAutoriseeDTO> partialUpdate(FormationAutoriseeDTO formationAutoriseeDTO) {
        log.debug("Request to partially update FormationAutorisee : {}", formationAutoriseeDTO);

        return formationAutoriseeRepository
            .findById(formationAutoriseeDTO.getId())
            .map(existingFormationAutorisee -> {
                formationAutoriseeMapper.partialUpdate(existingFormationAutorisee, formationAutoriseeDTO);

                return existingFormationAutorisee;
            })
            .map(formationAutoriseeRepository::save)
            .map(savedFormationAutorisee -> {
                formationAutoriseeSearchRepository.index(savedFormationAutorisee);
                return savedFormationAutorisee;
            })
            .map(formationAutoriseeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormationAutoriseeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FormationAutorisees");
        return formationAutoriseeRepository.findAll(pageable).map(formationAutoriseeMapper::toDto);
    }

    public Page<FormationAutoriseeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return formationAutoriseeRepository.findAllWithEagerRelationships(pageable).map(formationAutoriseeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormationAutoriseeDTO> findOne(Long id) {
        log.debug("Request to get FormationAutorisee : {}", id);
        return formationAutoriseeRepository.findOneWithEagerRelationships(id).map(formationAutoriseeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormationAutorisee : {}", id);
        formationAutoriseeRepository.deleteById(id);
        formationAutoriseeSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormationAutoriseeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FormationAutorisees for query {}", query);
        return formationAutoriseeSearchRepository.search(query, pageable).map(formationAutoriseeMapper::toDto);
    }

    /**
     * @param formationId
     * @param formationAutoriseeIds
     * @param dateDebut
     * @return
     */
    private static final Map<String, Integer> NIVEAU_MAP = Map.of(
        "L1", 1,
        "L2", 2,
        "L3", 3,
        "M1", 4,
        "M2", 5
    );

    @Override
    public FormationAutoriseeDTO definirFormationsAutorisees(Long formationId, List<Long> formationAutoriseeIds, LocalDate dateDebut) {

        Formation formation = formationRepository.findById(formationId)
            .orElseThrow(() -> new BadRequestAlertException("Cette Formation n'est pas présente " + formationId, ENTITY_NAME, "formationIntrouvable"));

        int currentNiveau = NIVEAU_MAP.getOrDefault(formation.getNiveau().getCodeNiveau(), -1);

        if (currentNiveau == -1) {
            throw new BadRequestAlertException("Niveau de la formation actuelle non valide", ENTITY_NAME, "invalidFormationNiveau");
        }

        String codeNiveauFormationActuelle = formation.getNiveau().getCodeNiveau();
        if ("L3".equals(codeNiveauFormationActuelle) || "M2".equals(codeNiveauFormationActuelle)) {
            if (!formationAutoriseeIds.isEmpty()) {
                throw new BadRequestAlertException("Cette formation ne peut pas avoir de formations N+1", ENTITY_NAME, "invalidNPlus1Formations");
            }
        } else {
            Set<Formation> formationsNiveauNPlus1 = formationRepository.findAllById(formationAutoriseeIds)
                .stream()
                .filter(f -> NIVEAU_MAP.getOrDefault(f.getNiveau().getCodeNiveau(), -1) == currentNiveau + 1)
                .collect(Collectors.toSet());

            if (formationsNiveauNPlus1.size() != formationAutoriseeIds.size()) {
                List<Long> invalidIds = formationAutoriseeIds.stream()
                    .filter(id -> formationsNiveauNPlus1.stream().noneMatch(f -> f.getId().equals(id)))
                    .collect(Collectors.toList());
                throw new BadRequestAlertException("Les formations suivantes ne sont pas de niveau N+1 : " + invalidIds, ENTITY_NAME, "invalidNPlus1Formation");
            }

            FormationAutorisee formationAutorisee = new FormationAutorisee();
            formationAutorisee.setId(formationId);
            formationAutorisee.setDateDebut(dateDebut);
            formationAutorisee.setFormations(formationsNiveauNPlus1);
            formationAutorisee.setEnCoursYN(true);
            formationAutorisee.setActifYN(true);

            FormationAutorisee result = formationAutoriseeRepository.save(formationAutorisee);

            return formationAutoriseeMapper.toDto(result);
        }
        return null;
    }


}
