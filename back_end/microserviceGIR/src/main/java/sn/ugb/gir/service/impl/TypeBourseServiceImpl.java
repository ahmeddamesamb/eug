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
import sn.ugb.gir.service.TypeBourseService;
import sn.ugb.gir.service.dto.TypeBourseDTO;
import sn.ugb.gir.service.mapper.TypeBourseMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;


/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeBourse}.
 */
@Service
@Transactional
public class TypeBourseServiceImpl implements TypeBourseService {

    private final Logger log = LoggerFactory.getLogger(TypeBourseServiceImpl.class);
    private static final String ENTITY_NAME = "typeBourse";

    private final TypeBourseRepository typeBourseRepository;
    private final TypeBourseMapper typeBourseMapper;

    public TypeBourseServiceImpl(TypeBourseRepository typeBourseRepository, TypeBourseMapper typeBourseMapper) {
        this.typeBourseRepository = typeBourseRepository;
        this.typeBourseMapper = typeBourseMapper;
    }

    @Override
    public TypeBourseDTO save(TypeBourseDTO typeBourseDTO) {
        log.debug("Request to save TypeBourse : {}", typeBourseDTO);

        validateData(typeBourseDTO.getLibelleTypeBourse(), typeBourseDTO.getId());

        TypeBourse typeBourse = typeBourseMapper.toEntity(typeBourseDTO);
        typeBourse = typeBourseRepository.save(typeBourse);
        return typeBourseMapper.toDto(typeBourse);
    }

    @Override
    public TypeBourseDTO update(TypeBourseDTO typeBourseDTO) {
        log.debug("Request to update TypeBourse : {}", typeBourseDTO);

        validateData(typeBourseDTO.getLibelleTypeBourse(), typeBourseDTO.getId());

        TypeBourse typeBourse = typeBourseMapper.toEntity(typeBourseDTO);
        typeBourse = typeBourseRepository.save(typeBourse);
        return typeBourseMapper.toDto(typeBourse);
    }

    @Override
    public Optional<TypeBourseDTO> partialUpdate(TypeBourseDTO typeBourseDTO) {
        log.debug("Request to partially update TypeBourse : {}", typeBourseDTO);

        validateData(typeBourseDTO.getLibelleTypeBourse(), typeBourseDTO.getId());

        return typeBourseRepository
            .findById(typeBourseDTO.getId())
            .map(existingTypeBourse -> {
                typeBourseMapper.partialUpdate(existingTypeBourse, typeBourseDTO);
                return existingTypeBourse;
            })
            .map(typeBourseRepository::save)
            .map(typeBourseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeBourseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeBourses");
        return typeBourseRepository.findAll(pageable).map(typeBourseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeBourseDTO> findOne(Long id) {
        log.debug("Request to get TypeBourse : {}", id);
        return typeBourseRepository.findById(id)
            .map(typeBourseMapper::toDto)
            .or(() -> {
                log.warn("TypeBourse with id {} not found", id);
                return Optional.empty();
            });
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeBourse : {}", id);

        if (!typeBourseRepository.existsById(id)) {
            throw new BadRequestAlertException("TypeBourse with id " + id + " not found", ENTITY_NAME, "typeBourseNotFound");
        }

        typeBourseRepository.deleteById(id);
    }

    private void validateData(String libelleTypeBourse, Long id) {
        if (libelleTypeBourse == null || libelleTypeBourse.trim().isBlank()) {
            throw new BadRequestAlertException("TypeBourse label cannot be empty", ENTITY_NAME, "emptyLibelleTypeBourse");
        }

        Optional<TypeBourse> existingTypeBourse = typeBourseRepository.findByLibelleTypeBourseIgnoreCase(libelleTypeBourse.trim());
        if (existingTypeBourse.isPresent() && !existingTypeBourse.get().getId().equals(id)) {
            throw new BadRequestAlertException("A TypeBourse with the same name already exists", ENTITY_NAME, "typeBourseExists");
        }
    }

}
