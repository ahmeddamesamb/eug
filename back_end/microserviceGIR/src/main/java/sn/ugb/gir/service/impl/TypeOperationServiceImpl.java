package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.TypeOperation;
import sn.ugb.gir.repository.TypeOperationRepository;
import sn.ugb.gir.service.TypeOperationService;
import sn.ugb.gir.service.dto.TypeOperationDTO;
import sn.ugb.gir.service.mapper.TypeOperationMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;


/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeOperation}.
 */
@Service
@Transactional
public class TypeOperationServiceImpl implements TypeOperationService {

    private final Logger log = LoggerFactory.getLogger(TypeOperationServiceImpl.class);
    private static final String ENTITY_NAME = "typeOperation";

    private final TypeOperationRepository typeOperationRepository;
    private final TypeOperationMapper typeOperationMapper;

    public TypeOperationServiceImpl(TypeOperationRepository typeOperationRepository, TypeOperationMapper typeOperationMapper) {
        this.typeOperationRepository = typeOperationRepository;
        this.typeOperationMapper = typeOperationMapper;
    }

    @Override
    public TypeOperationDTO save(TypeOperationDTO typeOperationDTO) {
        log.debug("Request to save TypeOperation : {}", typeOperationDTO);

        validateData(typeOperationDTO.getLibelleTypeOperation(), typeOperationDTO.getId());

        TypeOperation typeOperation = typeOperationMapper.toEntity(typeOperationDTO);
        typeOperation = typeOperationRepository.save(typeOperation);
        return typeOperationMapper.toDto(typeOperation);
    }

    @Override
    public TypeOperationDTO update(TypeOperationDTO typeOperationDTO) {
        log.debug("Request to update TypeOperation : {}", typeOperationDTO);

        validateData(typeOperationDTO.getLibelleTypeOperation(), typeOperationDTO.getId());

        TypeOperation typeOperation = typeOperationMapper.toEntity(typeOperationDTO);
        typeOperation = typeOperationRepository.save(typeOperation);
        return typeOperationMapper.toDto(typeOperation);
    }

    @Override
    public Optional<TypeOperationDTO> partialUpdate(TypeOperationDTO typeOperationDTO) {
        log.debug("Request to partially update TypeOperation : {}", typeOperationDTO);

        validateData(typeOperationDTO.getLibelleTypeOperation(), typeOperationDTO.getId());

        return typeOperationRepository
            .findById(typeOperationDTO.getId())
            .map(existingTypeOperation -> {
                typeOperationMapper.partialUpdate(existingTypeOperation, typeOperationDTO);
                return existingTypeOperation;
            })
            .map(typeOperationRepository::save)
            .map(typeOperationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeOperationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeOperations");
        return typeOperationRepository.findAll(pageable).map(typeOperationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeOperationDTO> findOne(Long id) {
        log.debug("Request to get TypeOperation : {}", id);
        return typeOperationRepository.findById(id)
            .map(typeOperationMapper::toDto)
            .or(() -> {
                log.warn("TypeOperation with id {} not found", id);
                return Optional.empty();
            });
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeOperation : {}", id);

        if (!typeOperationRepository.existsById(id)) {
            throw new BadRequestAlertException("TypeOperation with id " + id + " not found", ENTITY_NAME, "typeOperationNotFound");
        }

        typeOperationRepository.deleteById(id);
    }

    private void validateData(String libelleTypeOperation, Long id) {
        if (libelleTypeOperation == null || libelleTypeOperation.trim().isBlank()) {
            throw new BadRequestAlertException("TypeOperation label cannot be empty", ENTITY_NAME, "emptyLibelleTypeOperation");
        }

        Optional<TypeOperation> existingTypeOperation = typeOperationRepository.findByLibelleTypeOperationIgnoreCase(libelleTypeOperation.trim());
        if (existingTypeOperation.isPresent() && !existingTypeOperation.get().getId().equals(id)) {
            throw new BadRequestAlertException("A TypeOperation with the same name already exists", ENTITY_NAME, "typeOperationExists");
        }
    }

}
