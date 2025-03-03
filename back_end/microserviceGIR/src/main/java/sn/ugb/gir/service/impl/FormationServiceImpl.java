package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.enumeration.Cycle;
import sn.ugb.gir.repository.FormationRepository;
import sn.ugb.gir.service.FormationService;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.mapper.FormationMapper;
import sn.ugb.gir.domain.enumeration.TypeFormation;
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

    public FormationServiceImpl(FormationRepository formationRepository, FormationMapper formationMapper) {
        this.formationRepository = formationRepository;
        this.formationMapper = formationMapper;
    }

    @Override
    public FormationDTO save(FormationDTO formationDTO) {
        log.debug("Request to save Formation : {}", formationDTO);
        validateFormation(formationDTO);
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
    public Page<FormationDTO> findAllFormationByCycle(Cycle cycle, Pageable pageable) {
        return formationRepository.findByNiveauCycleNiveau(cycle, pageable)
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
        return formationRepository.findBySpecialiteMentionDomaineUfrsUniversiteIdAndTypeFormation(universiteId, TypeFormation.PUBLIC, pageable)
            .map(formationMapper::toDto);
    }

    @Override
    public Page<FormationDTO> findAllFormationPriveeByUniversite(Long universiteId, Pageable pageable) {
        return formationRepository.findBySpecialiteMentionDomaineUfrsUniversiteIdAndTypeFormation(universiteId, TypeFormation.PRIVEE, pageable)
            .map(formationMapper::toDto);
    }

    @Override
    public Page<FormationDTO> findAllFormationPubliqueByMinistere(Long ministereId, Pageable pageable) {
        return formationRepository.findBySpecialiteMentionDomaineUfrsUniversiteMinistereIdAndTypeFormation(ministereId, TypeFormation.PUBLIC, pageable)
            .map(formationMapper::toDto);
    }

    @Override
    public Page<FormationDTO> findAllFormationPriveeByMinistere(Long ministereId, Pageable pageable) {
        return formationRepository.findBySpecialiteMentionDomaineUfrsUniversiteMinistereIdAndTypeFormation(ministereId, TypeFormation.PRIVEE, pageable)
            .map(formationMapper::toDto);
    }

}
