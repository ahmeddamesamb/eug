package sn.ugb.gir.service.impl;

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
        validateData(disciplineSportiveEtudiantDTO);
        DisciplineSportiveEtudiant disciplineSportiveEtudiant = disciplineSportiveEtudiantMapper.toEntity(disciplineSportiveEtudiantDTO);
        disciplineSportiveEtudiant = disciplineSportiveEtudiantRepository.save(disciplineSportiveEtudiant);
        return disciplineSportiveEtudiantMapper.toDto(disciplineSportiveEtudiant);
    }

    @Override
    public DisciplineSportiveEtudiantDTO update(DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO, Long id) {
        log.debug("Request to update DisciplineSportiveEtudiant : {}", disciplineSportiveEtudiantDTO);

        validateData(disciplineSportiveEtudiantDTO);
        DisciplineSportiveEtudiant disciplineSportiveEtudiant = disciplineSportiveEtudiantMapper.toEntity(disciplineSportiveEtudiantDTO);
        disciplineSportiveEtudiant = disciplineSportiveEtudiantRepository.save(disciplineSportiveEtudiant);
        return disciplineSportiveEtudiantMapper.toDto(disciplineSportiveEtudiant);
    }

    @Override
    public Optional<DisciplineSportiveEtudiantDTO> partialUpdate(DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO, Long id) {
        log.debug("Request to partially update DisciplineSportiveEtudiant : {}", disciplineSportiveEtudiantDTO);

        validateData(disciplineSportiveEtudiantDTO);
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
        log.debug("Request to get all DisciplineSportiveEtudiants for an entity etudiant");
        return disciplineSportiveEtudiantRepository.findAllByEtudiantCodeEtu(pageable,codeEtu).map(disciplineSportiveEtudiantMapper::toDto);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<DisciplineSportiveEtudiantDTO> findAllByEtudiantId(Pageable pageable, Long id){
        log.debug("Request to get all DisciplineSportiveEtudiants by an id of etudiant");
        return disciplineSportiveEtudiantRepository.findAllByEtudiantId(pageable,id).map(disciplineSportiveEtudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DisciplineSportiveEtudiantDTO> findAllByDisciplineSportiveId(Pageable pageable, Long id) {
        log.debug("Request to get all DisciplineSportiveEtudiants for an id of a DisciplineSportive");
        return disciplineSportiveEtudiantRepository.findAllByDisciplineSportiveId(pageable,id).map(disciplineSportiveEtudiantMapper::toDto);
    }

    private void validateData(DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO){
        if (disciplineSportiveEtudiantDTO.getLicenceSportiveYN().describeConstable().isEmpty()) {
            throw new BadRequestAlertException("Veuillez renseigner si l'etudiant à une licence sportive (Oui/Non)", ENTITY_NAME, "licenceSportiveNull");
        }
        if (disciplineSportiveEtudiantDTO.getCompetitionUGBYN().describeConstable().isEmpty()) {
            throw new BadRequestAlertException("Veuillez renseigner si l'etudiant à fait une competition à l'UGB sportive (Oui/Non)", ENTITY_NAME, "competitionSportiveNull");
        }
        if (disciplineSportiveEtudiantDTO.getEtudiant().getId().describeConstable().isEmpty()) {
            throw new BadRequestAlertException("Veuillez renseigner l'id de l'etudiant", ENTITY_NAME, "etudiantNull");
        }
        if (disciplineSportiveEtudiantDTO.getDisciplineSportive().getId().describeConstable().isEmpty()) {
            throw new BadRequestAlertException("Veuillez renseigner l'id du discipline sportive", ENTITY_NAME, "disciplineSportiveEtudiantNull");
        }

        Long disciplineSportive = disciplineSportiveEtudiantDTO.getDisciplineSportive().getId();
        Long etudiant = disciplineSportiveEtudiantDTO.getEtudiant().getId();

        Optional <DisciplineSportiveEtudiant> existingDisciplineSportiveEtudiant =  disciplineSportiveEtudiantRepository.findByDisciplineSportiveIdAndEtudiantId(disciplineSportive,etudiant);
        if (existingDisciplineSportiveEtudiant.isPresent() && !existingDisciplineSportiveEtudiant.get().getId().equals(disciplineSportiveEtudiantDTO.getId())  ){
            throw new BadRequestAlertException("Cet etudiant est deja relier a ce discipline sportive", ENTITY_NAME, "disciplineSportiveEtudiantExiste");
        }
    }

}
