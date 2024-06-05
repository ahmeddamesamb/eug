package sn.ugb.gir.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Operateur;
import sn.ugb.gir.repository.OperateurRepository;
import sn.ugb.gir.service.OperateurService;
import sn.ugb.gir.service.dto.OperateurDTO;
import sn.ugb.gir.service.mapper.OperateurMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Operateur}.
 */
@Service
@Transactional
public class OperateurServiceImpl implements OperateurService {

    private final Logger log = LoggerFactory.getLogger(OperateurServiceImpl.class);

    private final OperateurRepository operateurRepository;

    private final OperateurMapper operateurMapper;

    public OperateurServiceImpl(OperateurRepository operateurRepository, OperateurMapper operateurMapper) {
        this.operateurRepository = operateurRepository;
        this.operateurMapper = operateurMapper;
    }

    @Override
    public OperateurDTO save(OperateurDTO operateurDTO) {
        log.debug("Request to save Operateur : {}", operateurDTO);

        if (operateurDTO.getId() != null) {
            throw new BadRequestAlertException("A new operateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (operateurDTO.getCodeOperateur().isEmpty() || operateurDTO.getCodeOperateur().isBlank() ) {
            throw new BadRequestAlertException("Veuillez renseigner le champs code operateur ", ENTITY_NAME, "codeOperateurobligatoire");
        }
        if ( operateurDTO.getUserLogin().isEmpty() || operateurDTO.getUserLogin().isBlank()) {
            throw new BadRequestAlertException("Veuillez renseigner le champs userLogin  ", ENTITY_NAME, "userLoginobligatoire");
        }
        if ( operateurDTO.getLibelleOperateur().isEmpty() || operateurDTO.getLibelleOperateur().isBlank()) {
            throw new BadRequestAlertException("Veuillez renseigner le champs libelle operateur  ", ENTITY_NAME, "libelleOperateurobligatoire");
        }

        if (  operateurRepository.existsOperateurByCodeOperateur(operateurDTO.getCodeOperateur())) {
            throw new BadRequestAlertException("Ce codeOperateur existe deja. Deux operateurs differents ne peuvent avoir le meme code. ", ENTITY_NAME, "codeoperateurexists");
        }
        if (  operateurRepository.existsOperateurByLibelleOperateur(operateurDTO.getLibelleOperateur())) {
            throw new BadRequestAlertException("Ce libelleOperateur existe deja. Deux operateurs differents ne peuvent avoir le meme libelle. ", ENTITY_NAME, "libelleoperateurexists");
        }


        Operateur operateur = operateurMapper.toEntity(operateurDTO);
        operateur = operateurRepository.save(operateur);
        return operateurMapper.toDto(operateur);
    }

    @Override
    public OperateurDTO update(OperateurDTO operateurDTO, Long id) {
        log.debug("Request to update Operateur : {}", operateurDTO);

        if (operateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Operateur operateur = operateurMapper.toEntity(operateurDTO);
        operateur = operateurRepository.save(operateur);
        return operateurMapper.toDto(operateur);
    }

    @Override
    public Optional<OperateurDTO> partialUpdate(OperateurDTO operateurDTO, Long id) {
        log.debug("Request to partially update Operateur : {}", operateurDTO);

        if (operateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        return operateurRepository
            .findById(operateurDTO.getId())
            .map(existingOperateur -> {
                operateurMapper.partialUpdate(existingOperateur, operateurDTO);

                return existingOperateur;
            })
            .map(operateurRepository::save)
            .map(operateurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OperateurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Operateurs");
        return operateurRepository.findAll(pageable).map(operateurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OperateurDTO> findOne(Long id) {
        log.debug("Request to get Operateur : {}", id);
        return operateurRepository.findById(id).map(operateurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Operateur : {}", id);
        operateurRepository.deleteById(id);
    }
}
