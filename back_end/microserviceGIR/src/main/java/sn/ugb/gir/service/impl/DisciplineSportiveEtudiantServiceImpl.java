package sn.ugb.gir.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.DisciplineSportiveEtudiant;
import sn.ugb.gir.repository.DisciplineSportiveEtudiantRepository;
import sn.ugb.gir.service.DisciplineSportiveEtudiantService;
import sn.ugb.gir.service.dto.DisciplineSportiveEtudiantDTO;
import sn.ugb.gir.service.mapper.DisciplineSportiveEtudiantMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.DisciplineSportiveEtudiant}.
 */
@Service
@Transactional
public class DisciplineSportiveEtudiantServiceImpl implements DisciplineSportiveEtudiantService {

    private final Logger log = LoggerFactory.getLogger(DisciplineSportiveEtudiantServiceImpl.class);

    private final DisciplineSportiveEtudiantRepository disciplineSportiveEtudiantRepository;

    private final DisciplineSportiveEtudiantMapper disciplineSportiveEtudiantMapper;

    public DisciplineSportiveEtudiantServiceImpl(
        DisciplineSportiveEtudiantRepository disciplineSportiveEtudiantRepository,
        DisciplineSportiveEtudiantMapper disciplineSportiveEtudiantMapper
    ) {
        this.disciplineSportiveEtudiantRepository = disciplineSportiveEtudiantRepository;
        this.disciplineSportiveEtudiantMapper = disciplineSportiveEtudiantMapper;
    }

    @Override
    public DisciplineSportiveEtudiantDTO save(DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO) {
        log.debug("Request to save DisciplineSportiveEtudiant : {}", disciplineSportiveEtudiantDTO);

        if (disciplineSportiveEtudiantDTO.getId() != null) {
            throw new BadRequestAlertException("A new disciplineSportiveEtudiant cannot already have an ID", ENTITY_NAME, "idexists");
        }

        DisciplineSportiveEtudiant disciplineSportiveEtudiant = disciplineSportiveEtudiantMapper.toEntity(disciplineSportiveEtudiantDTO);
        disciplineSportiveEtudiant = disciplineSportiveEtudiantRepository.save(disciplineSportiveEtudiant);
        return disciplineSportiveEtudiantMapper.toDto(disciplineSportiveEtudiant);
    }

    @Override
    public DisciplineSportiveEtudiantDTO update(DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO, Long id) {
        log.debug("Request to update DisciplineSportiveEtudiant : {}", disciplineSportiveEtudiantDTO);

        if (disciplineSportiveEtudiantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disciplineSportiveEtudiantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disciplineSportiveEtudiantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DisciplineSportiveEtudiant disciplineSportiveEtudiant = disciplineSportiveEtudiantMapper.toEntity(disciplineSportiveEtudiantDTO);
        disciplineSportiveEtudiant = disciplineSportiveEtudiantRepository.save(disciplineSportiveEtudiant);
        return disciplineSportiveEtudiantMapper.toDto(disciplineSportiveEtudiant);
    }

    @Override
    public Optional<DisciplineSportiveEtudiantDTO> partialUpdate(DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO, Long id) {
        log.debug("Request to partially update DisciplineSportiveEtudiant : {}", disciplineSportiveEtudiantDTO);

        if (disciplineSportiveEtudiantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disciplineSportiveEtudiantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disciplineSportiveEtudiantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        return disciplineSportiveEtudiantRepository
            .findById(disciplineSportiveEtudiantDTO.getId())
            .map(existingDisciplineSportiveEtudiant -> {
                disciplineSportiveEtudiantMapper.partialUpdate(existingDisciplineSportiveEtudiant, disciplineSportiveEtudiantDTO);

                return existingDisciplineSportiveEtudiant;
            })
            .map(disciplineSportiveEtudiantRepository::save)
            .map(disciplineSportiveEtudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DisciplineSportiveEtudiantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DisciplineSportiveEtudiants");
        return disciplineSportiveEtudiantRepository.findAll(pageable).map(disciplineSportiveEtudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DisciplineSportiveEtudiantDTO> findOne(Long id) {
        log.debug("Request to get DisciplineSportiveEtudiant : {}", id);
        return disciplineSportiveEtudiantRepository.findById(id).map(disciplineSportiveEtudiantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DisciplineSportiveEtudiant : {}", id);
        disciplineSportiveEtudiantRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DisciplineSportiveEtudiantDTO> findAllByEtudiantCodeEtu(Pageable pageable, String codeEtu){
        log.debug("Request to get all DisciplineSportiveEtudiants for an et entity etudiant");
        return disciplineSportiveEtudiantRepository.findAllByEtudiantCodeEtu(pageable,codeEtu).map(disciplineSportiveEtudiantMapper::toDto);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<DisciplineSportiveEtudiantDTO> findAllByEtudiantId(Pageable pageable, Long id){
        log.debug("Request to get all DisciplineSportiveEtudiants");
        return disciplineSportiveEtudiantRepository.findAllByEtudiantId(pageable,id).map(disciplineSportiveEtudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DisciplineSportiveEtudiantDTO> findAllByDisciplineSportiveId(Pageable pageable, Long id) {
        log.debug("Request to get all DisciplineSportiveEtudiants for an id of a DisciplineSportive");
        return disciplineSportiveEtudiantRepository.findAllByDisciplineSportiveId(pageable,id).map(disciplineSportiveEtudiantMapper::toDto);
    }
}
