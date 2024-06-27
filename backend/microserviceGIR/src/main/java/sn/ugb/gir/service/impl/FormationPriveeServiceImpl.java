package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.FormationPrivee;
import sn.ugb.gir.repository.FormationPriveeRepository;
import sn.ugb.gir.repository.FormationRepository;
import sn.ugb.gir.repository.search.FormationPriveeSearchRepository;
import sn.ugb.gir.service.FormationPriveeService;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.dto.FormationPriveeDTO;
import sn.ugb.gir.service.mapper.FormationMapper;
import sn.ugb.gir.service.mapper.FormationPriveeMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.FormationPrivee}.
 */
@Service
@Transactional
public class FormationPriveeServiceImpl implements FormationPriveeService {

    private final Logger log = LoggerFactory.getLogger(FormationPriveeServiceImpl.class);

    private static final String ENTITY_NAME = "FormationPrivee";

    private final FormationPriveeRepository formationPriveeRepository;

    private final FormationPriveeMapper formationPriveeMapper;

    private final FormationPriveeSearchRepository formationPriveeSearchRepository;


    private final FormationMapper formationMapper;

    private final FormationRepository formationRepository;

    public FormationPriveeServiceImpl(
        FormationPriveeRepository formationPriveeRepository,
        FormationPriveeMapper formationPriveeMapper,
        FormationPriveeSearchRepository formationPriveeSearchRepository, FormationMapper formationMapper, FormationRepository formationRepository
    ) {
        this.formationPriveeRepository = formationPriveeRepository;
        this.formationPriveeMapper = formationPriveeMapper;
        this.formationPriveeSearchRepository = formationPriveeSearchRepository;
        this.formationMapper = formationMapper;
        this.formationRepository = formationRepository;
    }

    @Transactional
    @Override
    public FormationPriveeDTO save(FormationPriveeDTO formationPriveeDTO) {
        log.debug("Request to save FormationPrivee : {}", formationPriveeDTO);

        validateFormationPrivee(formationPriveeDTO);
        FormationDTO formationDTO = formationPriveeDTO.getFormation();

        Formation formation = formationMapper.toEntity(formationDTO);
        formation = formationRepository.save(formation);

        if (formation.getTypeFormation().getLibelleTypeFormation().equals("PRIVEE")) {
            formationPriveeDTO.setFormation(formationMapper.toDto(formation));
            FormationPrivee formationPrivee = formationPriveeMapper.toEntity(formationPriveeDTO);
            formationPrivee = formationPriveeRepository.save(formationPrivee);
            return formationPriveeMapper.toDto(formationPrivee);
        } else if (formation.getTypeFormation().getLibelleTypeFormation().equals("PUBLIC")) {
            FormationPriveeDTO emptyFormationPriveeDTO = new FormationPriveeDTO();
            emptyFormationPriveeDTO.setId(0L);
            return emptyFormationPriveeDTO;
        } else {
            throw new BadRequestAlertException("Type de formation inconnu : " + formation.getTypeFormation().getLibelleTypeFormation(), ENTITY_NAME, "typeFormationInconnu");
        }
    }

    @Transactional
    @Override
    public FormationPriveeDTO update(FormationPriveeDTO formationPriveeDTO) {
        log.debug("Request to update FormationPrivee : {}", formationPriveeDTO);

        validateFormationPrivee(formationPriveeDTO);
        FormationPrivee formationPrivee = formationPriveeMapper.toEntity(formationPriveeDTO);
        formationPrivee = formationPriveeRepository.save(formationPrivee);
        return formationPriveeMapper.toDto(formationPrivee);
    }

    @Override
    public Optional<FormationPriveeDTO> partialUpdate(FormationPriveeDTO formationPriveeDTO) {
        log.debug("Request to partially update FormationPrivee : {}", formationPriveeDTO);

        return formationPriveeRepository
            .findById(formationPriveeDTO.getId())
            .map(existingFormationPrivee -> {
                formationPriveeMapper.partialUpdate(existingFormationPrivee, formationPriveeDTO);

                return existingFormationPrivee;
            })
            .map(formationPriveeRepository::save)
            .map(savedFormationPrivee -> {
                formationPriveeSearchRepository.index(savedFormationPrivee);
                return savedFormationPrivee;
            })
            .map(formationPriveeMapper::toDto);
    }

    private void validateFormationPrivee(FormationPriveeDTO formationPriveeDTO) {
        FormationDTO formationDTO = formationPriveeDTO.getFormation();

        if (formationDTO.getNiveau() == null) {
            throw new BadRequestAlertException("Le niveau de la formation ne doit pas être vide.", ENTITY_NAME, "niveauNotNull");
        }
        if (formationDTO.getSpecialite() == null) {
            throw new BadRequestAlertException("La spécialité de la formation ne doit pas être vide.", ENTITY_NAME, "specialiteNotNull");
        }
        Optional<Formation> existingFormation = formationRepository.findByNiveauIdAndSpecialiteId(formationDTO.getNiveau().getId(), formationDTO.getSpecialite().getId());
        if (existingFormation.isPresent() && !existingFormation.get().getId().equals(formationDTO.getId())) {
            throw new BadRequestAlertException("Cette Formation existe déjà avec le même Niveau et la même Spécialité.", ENTITY_NAME, "formationExists");
        }

        if ("PRIVEE".equals(formationDTO.getTypeFormation().getLibelleTypeFormation())){
            if (formationPriveeDTO.getNombreMensualites() == null) {
                throw new BadRequestAlertException("Le champ 'nombreMensualites' ne doit pas être nul pour une formation privée.", ENTITY_NAME, "nombreMensualitesNotNull");
            }
            if (formationPriveeDTO.getPaiementPremierMoisYN() == null) {
                throw new BadRequestAlertException("Le champ 'paiementPremierMoisYN' ne doit pas être nul pour une formation privée.", ENTITY_NAME, "paiementPremierMoisYNNotNull");
            }
            if (formationPriveeDTO.getPaiementDernierMoisYN() == null) {
                throw new BadRequestAlertException("Le champ 'paiementDernierMoisYN' ne doit pas être nul pour une formation privée.", ENTITY_NAME, "paiementDernierMoisYNNotNull");
            }
            if (formationPriveeDTO.getFraisDossierYN() == null) {
                throw new BadRequestAlertException("Le champ 'fraisDossierYN' ne doit pas être nul pour une formation privée.", ENTITY_NAME, "fraisDossierYNNotNull");
            }
            if (formationPriveeDTO.getCoutTotal() == null) {
                throw new BadRequestAlertException("Le champ 'coutTotal' ne doit pas être nul pour une formation privée.", ENTITY_NAME, "coutTotalNotNull");
            }
            if (formationPriveeDTO.getMensualite() == null) {
                throw new BadRequestAlertException("Le champ 'mensualite' ne doit pas être nul pour une formation privée.", ENTITY_NAME, "mensualiteNotNull");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormationPriveeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FormationPrivees");
        return formationPriveeRepository.findAll(pageable).map(formationPriveeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormationPriveeDTO> findOne(Long id) {
        log.debug("Request to get FormationPrivee : {}", id);
        return formationPriveeRepository.findById(id).map(formationPriveeMapper::toDto);
    }


    @Transactional
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormationPrivee : {}", id);

        Optional<FormationPrivee> optionalFormationPrivee = formationPriveeRepository.findById(id);

        if (optionalFormationPrivee.isPresent()) {
            FormationPrivee formationPrivee = optionalFormationPrivee.get();

            formationPriveeRepository.deleteById(id);
            log.debug("Formation privée avec ID {} supprimée avec succès.", id);

            Long formationId = formationPrivee.getFormation().getId();
            formationRepository.deleteById(formationId);
            log.debug("Formation avec ID {} supprimée avec succès.", formationId);

        } else {
            log.debug("La formation privée avec ID {} n'existe pas.", id);
        }
        formationPriveeSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormationPriveeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FormationPrivees for query {}", query);
        return formationPriveeSearchRepository.search(query, pageable).map(formationPriveeMapper::toDto);
    }
}
