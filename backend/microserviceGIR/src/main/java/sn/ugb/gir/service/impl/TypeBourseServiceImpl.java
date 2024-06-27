package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.TypeBourse;
import sn.ugb.gir.repository.TypeBourseRepository;
import sn.ugb.gir.repository.search.TypeBourseSearchRepository;
import sn.ugb.gir.service.TypeBourseService;
import sn.ugb.gir.service.dto.TypeBourseDTO;
import sn.ugb.gir.service.mapper.TypeBourseMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Implémentation du service pour la gestion des {@link sn.ugb.gir.domain.TypeBourse}.
 */
@Service
@Transactional
public class TypeBourseServiceImpl implements TypeBourseService {

    private final Logger log = LoggerFactory.getLogger(TypeBourseServiceImpl.class);

    private final TypeBourseRepository typeHandicapRepository;

    private final TypeBourseMapper typeHandicapMapper;

    private final TypeBourseSearchRepository typeHandicapSearchRepository;

    public TypeBourseServiceImpl(
        TypeBourseRepository typeHandicapRepository,
        TypeBourseMapper typeHandicapMapper,
        TypeBourseSearchRepository typeHandicapSearchRepository
    ) {
        this.typeHandicapRepository = typeHandicapRepository;
        this.typeHandicapMapper = typeHandicapMapper;
        this.typeHandicapSearchRepository = typeHandicapSearchRepository;
    }

    @Override
    public TypeBourseDTO save(TypeBourseDTO typeHandicapDTO) {
        log.debug("Requête pour enregistrer TypeBourse : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeBourse(), typeHandicapDTO.getId());

        TypeBourse typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        TypeBourseDTO result = typeHandicapMapper.toDto(typeHandicap);
        typeHandicapSearchRepository.index(typeHandicap);
        return result;
    }

    @Override
    public TypeBourseDTO update(TypeBourseDTO typeHandicapDTO) {
        log.debug("Requête pour mettre à jour TypeBourse : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeBourse(), typeHandicapDTO.getId());

        TypeBourse typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        TypeBourseDTO result = typeHandicapMapper.toDto(typeHandicap);
        typeHandicapSearchRepository.index(typeHandicap);
        return result;
    }

    @Override
    public Optional<TypeBourseDTO> partialUpdate(TypeBourseDTO typeHandicapDTO) {
        log.debug("Requête pour mettre à jour partiellement TypeBourse : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeBourse(), typeHandicapDTO.getId());

        return typeHandicapRepository
            .findById(typeHandicapDTO.getId())
            .map(existingTypeBourse -> {
                typeHandicapMapper.partialUpdate(existingTypeBourse, typeHandicapDTO);
                return existingTypeBourse;
            })
            .map(typeHandicapRepository::save)
            .map(savedTypeBourse -> {
                typeHandicapSearchRepository.index(savedTypeBourse);
                return typeHandicapMapper.toDto(savedTypeBourse);
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeBourseDTO> findAll(Pageable pageable) {
        log.debug("Requête pour récupérer tous les TypeBourses");
        return typeHandicapRepository.findAll(pageable).map(typeHandicapMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeBourseDTO> findOne(Long id) {
        log.debug("Requête pour récupérer TypeBourse : {}", id);
        return typeHandicapRepository.findById(id).map(typeHandicapMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Requête pour supprimer TypeBourse : {}", id);
        typeHandicapRepository.deleteById(id);
        typeHandicapSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeBourseDTO> search(String query, Pageable pageable) {
        log.debug("Requête pour rechercher une page de TypeBourses pour la requête {}", query);
        return typeHandicapSearchRepository.search(query, pageable).map(typeHandicapMapper::toDto);
    }

    private void validateData(String libelleTypeBourse, Long id) {
        if (libelleTypeBourse == null || libelleTypeBourse.trim().isBlank()) {
            throw new BadRequestAlertException("Le libellé du TypeBourse ne peut pas être vide", ENTITY_NAME, "libelleTypeBourseVide");
        }

        Optional<TypeBourse> existingTypeBourse = typeHandicapRepository.findByLibelleTypeBourseIgnoreCase(libelleTypeBourse.trim());
        if (existingTypeBourse.isPresent() && !existingTypeBourse.get().getId().equals(id)) {
            throw new BadRequestAlertException("Un TypeBourse avec le même nom existe déjà", ENTITY_NAME, "typeHandicapExiste");
        }
    }
}
