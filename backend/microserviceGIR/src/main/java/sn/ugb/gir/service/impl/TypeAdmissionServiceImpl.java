package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.TypeAdmission;
import sn.ugb.gir.repository.TypeAdmissionRepository;
import sn.ugb.gir.service.TypeAdmissionService;
import sn.ugb.gir.service.dto.TypeAdmissionDTO;
import sn.ugb.gir.service.mapper.TypeAdmissionMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeAdmission}.
 */
@Service
@Transactional
public class TypeAdmissionServiceImpl implements TypeAdmissionService {

    private final Logger log = LoggerFactory.getLogger(TypeAdmissionServiceImpl.class);

    private final TypeAdmissionRepository typeAdmissionRepository;

    private final TypeAdmissionMapper typeAdmissionMapper;

    public TypeAdmissionServiceImpl(TypeAdmissionRepository typeAdmissionRepository, TypeAdmissionMapper typeAdmissionMapper) {
        this.typeAdmissionRepository = typeAdmissionRepository;
        this.typeAdmissionMapper = typeAdmissionMapper;
    }

    @Override
    public TypeAdmissionDTO save(TypeAdmissionDTO typeAdmissionDTO) {
        log.debug("Request to save TypeAdmission : {}", typeAdmissionDTO);
        TypeAdmission typeAdmission = typeAdmissionMapper.toEntity(typeAdmissionDTO);
        typeAdmission = typeAdmissionRepository.save(typeAdmission);
        return typeAdmissionMapper.toDto(typeAdmission);
    }

    @Override
    public TypeAdmissionDTO update(TypeAdmissionDTO typeAdmissionDTO) {
        log.debug("Request to update TypeAdmission : {}", typeAdmissionDTO);
        TypeAdmission typeAdmission = typeAdmissionMapper.toEntity(typeAdmissionDTO);
        typeAdmission = typeAdmissionRepository.save(typeAdmission);
        return typeAdmissionMapper.toDto(typeAdmission);
    }

    @Override
    public Optional<TypeAdmissionDTO> partialUpdate(TypeAdmissionDTO typeAdmissionDTO) {
        log.debug("Request to partially update TypeAdmission : {}", typeAdmissionDTO);

        return typeAdmissionRepository
            .findById(typeAdmissionDTO.getId())
            .map(existingTypeAdmission -> {
                typeAdmissionMapper.partialUpdate(existingTypeAdmission, typeAdmissionDTO);

                return existingTypeAdmission;
            })
            .map(typeAdmissionRepository::save)
            .map(typeAdmissionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeAdmissionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeAdmissions");
        return typeAdmissionRepository.findAll(pageable).map(typeAdmissionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeAdmissionDTO> findOne(Long id) {
        log.debug("Request to get TypeAdmission : {}", id);
        return typeAdmissionRepository.findById(id).map(typeAdmissionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeAdmission : {}", id);
        typeAdmissionRepository.deleteById(id);
    }
}
