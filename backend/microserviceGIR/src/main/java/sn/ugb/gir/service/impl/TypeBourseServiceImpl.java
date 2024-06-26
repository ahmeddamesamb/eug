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

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeBourse}.
 */
@Service
@Transactional
public class TypeBourseServiceImpl implements TypeBourseService {

    private final Logger log = LoggerFactory.getLogger(TypeBourseServiceImpl.class);

    private final TypeBourseRepository typeBourseRepository;

    private final TypeBourseMapper typeBourseMapper;

    private final TypeBourseSearchRepository typeBourseSearchRepository;

    public TypeBourseServiceImpl(
        TypeBourseRepository typeBourseRepository,
        TypeBourseMapper typeBourseMapper,
        TypeBourseSearchRepository typeBourseSearchRepository
    ) {
        this.typeBourseRepository = typeBourseRepository;
        this.typeBourseMapper = typeBourseMapper;
        this.typeBourseSearchRepository = typeBourseSearchRepository;
    }

    @Override
    public TypeBourseDTO save(TypeBourseDTO typeBourseDTO) {
        log.debug("Request to save TypeBourse : {}", typeBourseDTO);
        TypeBourse typeBourse = typeBourseMapper.toEntity(typeBourseDTO);
        typeBourse = typeBourseRepository.save(typeBourse);
        TypeBourseDTO result = typeBourseMapper.toDto(typeBourse);
        typeBourseSearchRepository.index(typeBourse);
        return result;
    }

    @Override
    public TypeBourseDTO update(TypeBourseDTO typeBourseDTO) {
        log.debug("Request to update TypeBourse : {}", typeBourseDTO);
        TypeBourse typeBourse = typeBourseMapper.toEntity(typeBourseDTO);
        typeBourse = typeBourseRepository.save(typeBourse);
        TypeBourseDTO result = typeBourseMapper.toDto(typeBourse);
        typeBourseSearchRepository.index(typeBourse);
        return result;
    }

    @Override
    public Optional<TypeBourseDTO> partialUpdate(TypeBourseDTO typeBourseDTO) {
        log.debug("Request to partially update TypeBourse : {}", typeBourseDTO);

        return typeBourseRepository
            .findById(typeBourseDTO.getId())
            .map(existingTypeBourse -> {
                typeBourseMapper.partialUpdate(existingTypeBourse, typeBourseDTO);

                return existingTypeBourse;
            })
            .map(typeBourseRepository::save)
            .map(savedTypeBourse -> {
                typeBourseSearchRepository.index(savedTypeBourse);
                return savedTypeBourse;
            })
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
        typeBourseSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeBourseDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TypeBourses for query {}", query);
        return typeBourseSearchRepository.search(query, pageable).map(typeBourseMapper::toDto);
    }
}
