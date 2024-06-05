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

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeBourse}.
 */
@Service
@Transactional
public class TypeBourseServiceImpl implements TypeBourseService {

    private final Logger log = LoggerFactory.getLogger(TypeBourseServiceImpl.class);

    private final TypeBourseRepository typeBourseRepository;

    private final TypeBourseMapper typeBourseMapper;

    public TypeBourseServiceImpl(TypeBourseRepository typeBourseRepository, TypeBourseMapper typeBourseMapper) {
        this.typeBourseRepository = typeBourseRepository;
        this.typeBourseMapper = typeBourseMapper;
    }

    @Override
    public TypeBourseDTO save(TypeBourseDTO typeBourseDTO) {
        log.debug("Request to save TypeBourse : {}", typeBourseDTO);

        if(typeBourseRepository.findByLibelleTypeBourse(typeBourseDTO.getLibelleTypeBourse()).isPresent()){
            throw new BadRequestAlertException("A new niveau have an TypeBourse exists ", ENTITY_NAME, "TypeBourse exists");
        }

        TypeBourse typeBourse = typeBourseMapper.toEntity(typeBourseDTO);
        typeBourse = typeBourseRepository.save(typeBourse);
        return typeBourseMapper.toDto(typeBourse);
    }

    @Override
    public TypeBourseDTO update(TypeBourseDTO typeBourseDTO) {
        log.debug("Request to update TypeBourse : {}", typeBourseDTO);

        if(typeBourseRepository.findByLibelleTypeBourseAndIdNot(typeBourseDTO.getLibelleTypeBourse(), typeBourseDTO.getId()).isPresent()){
            throw new BadRequestAlertException("A update Bourse have an TypeBourse exists ", ENTITY_NAME, "TypeBourse exists");
        }
        TypeBourse typeBourse = typeBourseMapper.toEntity(typeBourseDTO);
        typeBourse = typeBourseRepository.save(typeBourse);
        return typeBourseMapper.toDto(typeBourse);
    }

    @Override
    public Optional<TypeBourseDTO> partialUpdate(TypeBourseDTO typeBourseDTO) {
        log.debug("Request to partially update TypeBourse : {}", typeBourseDTO);

        if(typeBourseRepository.findByLibelleTypeBourseAndIdNot(typeBourseDTO.getLibelleTypeBourse(), typeBourseDTO.getId()).isPresent()){
            throw new BadRequestAlertException("A update Bourse have an TypeBourse exists ", ENTITY_NAME, "TypeBourse exists");
        }

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
        return typeBourseRepository.findById(id).map(typeBourseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeBourse : {}", id);
        typeBourseRepository.deleteById(id);
    }
}
