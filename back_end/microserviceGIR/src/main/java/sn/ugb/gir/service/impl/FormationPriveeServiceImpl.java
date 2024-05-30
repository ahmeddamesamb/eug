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
import sn.ugb.gir.domain.enumeration.TypeFormation;
import sn.ugb.gir.repository.FormationPriveeRepository;
import sn.ugb.gir.repository.FormationRepository;
import sn.ugb.gir.service.FormationPriveeService;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.dto.FormationPriveeDTO;
import sn.ugb.gir.service.mapper.FormationMapper;
import sn.ugb.gir.service.mapper.FormationPriveeMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.FormationPrivee}.
 */
@Service
@Transactional
public class FormationPriveeServiceImpl implements FormationPriveeService {

    private final Logger log = LoggerFactory.getLogger(FormationPriveeServiceImpl.class);

    private final FormationPriveeRepository formationPriveeRepository;

    private final FormationPriveeMapper formationPriveeMapper;

    private final FormationMapper formationMapper;

    private final FormationRepository formationRepository;



    public FormationPriveeServiceImpl(FormationPriveeRepository formationPriveeRepository, FormationPriveeMapper formationPriveeMapper, FormationMapper formationMapper, FormationRepository formationRepository) {
        this.formationPriveeRepository = formationPriveeRepository;
        this.formationPriveeMapper = formationPriveeMapper;
        this.formationMapper = formationMapper;
        this.formationRepository = formationRepository;
    }

    @Transactional
    @Override
    public FormationPriveeDTO save(FormationPriveeDTO formationPriveeDTO) {
        log.debug("Request to save FormationPrivee : {}", formationPriveeDTO);

        FormationDTO formationDTO = formationPriveeDTO.getFormation();
        if (formationDTO == null) {
            throw new IllegalArgumentException("La formation ne doit pas être nulle.");
        }

        if (formationDTO.getNiveau() == null || formationDTO.getSpecialite() == null) {
            throw new IllegalArgumentException("Le niveau et la spécialité ne doivent pas être null.");
        }

        Optional<Formation> existingFormation = formationRepository.findByNiveauIdAndSpecialiteId(formationDTO.getNiveau().getId(), formationDTO.getSpecialite().getId());

        if (existingFormation.isPresent()) {
            throw new IllegalArgumentException("Cette Formation existe déjà avec le même Niveau et la même Spécialité.");
        }

        if (formationDTO.getTypeFormation() == TypeFormation.PRIVEE) {
            if (formationPriveeDTO.getNombreMensualites() == null ||
                formationPriveeDTO.getPaiementPremierMoisYN() == null ||
                formationPriveeDTO.getPaiementDernierMoisYN() == null ||
                formationPriveeDTO.getFraisDossierYN() == null ||
                formationPriveeDTO.getCoutTotal() == null ||
                formationPriveeDTO.getMensualite() == null) {
                throw new IllegalArgumentException("Les champs 'nombreMensualites', 'paiementPremierMoisYN', 'paiementDernierMoisYN', 'fraisDossierYN', 'coutTotal', et 'mensualite' ne doivent pas être nuls pour une formation privée.");
            }
        }

        Formation formation = formationMapper.toEntity(formationDTO);
        formation = formationRepository.save(formation);

        if (formationDTO.getTypeFormation() == TypeFormation.PRIVEE) {
            formationPriveeDTO.setFormation(formationMapper.toDto(formation));
            FormationPrivee formationPrivee = formationPriveeMapper.toEntity(formationPriveeDTO);
            formationPrivee = formationPriveeRepository.save(formationPrivee);
            return formationPriveeMapper.toDto(formationPrivee);
        } else if (formationDTO.getTypeFormation() == TypeFormation.PUBLIC) {
            FormationPriveeDTO emptyFormationPriveeDTO = new FormationPriveeDTO();
            emptyFormationPriveeDTO.setId(0L);
            return emptyFormationPriveeDTO;
        } else {
            throw new IllegalArgumentException("Type de formation inconnu : " + formationDTO.getTypeFormation());
        }
    }


    @Override
    public FormationPriveeDTO update(FormationPriveeDTO formationPriveeDTO) {
        log.debug("Request to update FormationPrivee : {}", formationPriveeDTO);
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
            .map(formationPriveeMapper::toDto);
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
    }

}
