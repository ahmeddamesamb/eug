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
import sn.ugb.gir.repository.search.TypeAdmissionSearchRepository;
import sn.ugb.gir.service.TypeAdmissionService;
import sn.ugb.gir.service.dto.TypeAdmissionDTO;
import sn.ugb.gir.service.mapper.TypeAdmissionMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeAdmission}.
 */
@Service
@Transactional
public class TypeAdmissionServiceImpl implements TypeAdmissionService {

    private final Logger log = LoggerFactory.getLogger(TypeAdmissionServiceImpl.class);

    private final TypeAdmissionRepository typeAdmissionRepository;

    private final TypeAdmissionMapper typeAdmissionMapper;

    private final TypeAdmissionSearchRepository typeAdmissionSearchRepository;

    private static final String ENTITY_NAME = "microserviceGirTypeAdmission";

    public TypeAdmissionServiceImpl(
        TypeAdmissionRepository typeAdmissionRepository,
        TypeAdmissionMapper typeAdmissionMapper,
        TypeAdmissionSearchRepository typeAdmissionSearchRepository
    ) {
        this.typeAdmissionRepository = typeAdmissionRepository;
        this.typeAdmissionMapper = typeAdmissionMapper;
        this.typeAdmissionSearchRepository = typeAdmissionSearchRepository;
    }

    @Override
    public TypeAdmissionDTO save(TypeAdmissionDTO typeAdmissionDTO) {
        log.debug("Request to save TypeAdmission : {}", typeAdmissionDTO);
        validateData(typeAdmissionDTO);
        TypeAdmission typeAdmission = typeAdmissionMapper.toEntity(typeAdmissionDTO);
        typeAdmission = typeAdmissionRepository.save(typeAdmission);
        TypeAdmissionDTO result = typeAdmissionMapper.toDto(typeAdmission);
        typeAdmissionSearchRepository.index(typeAdmission);
        return result;
    }

    @Override
    public TypeAdmissionDTO update(TypeAdmissionDTO typeAdmissionDTO) {
        log.debug("Request to update TypeAdmission : {}", typeAdmissionDTO);
        validateData(typeAdmissionDTO);
        TypeAdmission typeAdmission = typeAdmissionMapper.toEntity(typeAdmissionDTO);
        typeAdmission = typeAdmissionRepository.save(typeAdmission);
        TypeAdmissionDTO result = typeAdmissionMapper.toDto(typeAdmission);
        typeAdmissionSearchRepository.index(typeAdmission);
        return result;
    }

    @Override
    public Optional<TypeAdmissionDTO> partialUpdate(TypeAdmissionDTO typeAdmissionDTO) {
        log.debug("Request to partially update TypeAdmission : {}", typeAdmissionDTO);
        validateData(typeAdmissionDTO);
        return typeAdmissionRepository
            .findById(typeAdmissionDTO.getId())
            .map(existingTypeAdmission -> {
                typeAdmissionMapper.partialUpdate(existingTypeAdmission, typeAdmissionDTO);

                return existingTypeAdmission;
            })
            .map(typeAdmissionRepository::save)
            .map(savedTypeAdmission -> {
                typeAdmissionSearchRepository.index(savedTypeAdmission);
                return savedTypeAdmission;
            })
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
        typeAdmissionSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeAdmissionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TypeAdmissions for query {}", query);
        return typeAdmissionSearchRepository.search(query, pageable).map(typeAdmissionMapper::toDto);
    }

    private void validateData(TypeAdmissionDTO typeAdmissionDto) {
        if (typeAdmissionDto.getLibelleTypeAdmission().isBlank()){
            throw new BadRequestAlertException("Le libellé ne peut pas être vide.", ENTITY_NAME, "libelleTypeAdmissionNotNull");
        }
        Optional<TypeAdmission> existingTypeAdmission = typeAdmissionRepository.findByLibelleTypeAdmissionIgnoreCase(typeAdmissionDto.getLibelleTypeAdmission());
        if (existingTypeAdmission.isPresent() && !existingTypeAdmission.get().getId().equals(typeAdmissionDto.getId())) {
            throw new BadRequestAlertException("Un type admission avec le même libellé existe.", ENTITY_NAME, "libelleTypeAdmissionExist");
        }
    }
}
