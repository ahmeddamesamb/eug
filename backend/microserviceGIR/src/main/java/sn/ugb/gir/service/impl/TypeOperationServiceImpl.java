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
import sn.ugb.gir.repository.search.TypeOperationSearchRepository;
import sn.ugb.gir.service.TypeOperationService;
import sn.ugb.gir.service.dto.TypeOperationDTO;
import sn.ugb.gir.service.mapper.TypeOperationMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeOperation}.
 */
@Service
@Transactional
public class TypeOperationServiceImpl implements TypeOperationService {

    private final Logger log = LoggerFactory.getLogger(TypeOperationServiceImpl.class);

    private final TypeOperationRepository typeOperationRepository;

    private final TypeOperationMapper typeOperationMapper;

    private final TypeOperationSearchRepository typeOperationSearchRepository;

    public TypeOperationServiceImpl(
        TypeOperationRepository typeOperationRepository,
        TypeOperationMapper typeOperationMapper,
        TypeOperationSearchRepository typeOperationSearchRepository
    ) {
        this.typeOperationRepository = typeOperationRepository;
        this.typeOperationMapper = typeOperationMapper;
        this.typeOperationSearchRepository = typeOperationSearchRepository;
    }

    @Override
    public TypeOperationDTO save(TypeOperationDTO typeOperationDTO) {
        log.debug("Request to save TypeOperation : {}", typeOperationDTO);
        TypeOperation typeOperation = typeOperationMapper.toEntity(typeOperationDTO);
        typeOperation = typeOperationRepository.save(typeOperation);
        TypeOperationDTO result = typeOperationMapper.toDto(typeOperation);
        typeOperationSearchRepository.index(typeOperation);
        return result;
    }

    @Override
    public TypeOperationDTO update(TypeOperationDTO typeOperationDTO) {
        log.debug("Request to update TypeOperation : {}", typeOperationDTO);
        TypeOperation typeOperation = typeOperationMapper.toEntity(typeOperationDTO);
        typeOperation = typeOperationRepository.save(typeOperation);
        TypeOperationDTO result = typeOperationMapper.toDto(typeOperation);
        typeOperationSearchRepository.index(typeOperation);
        return result;
    }

    @Override
    public Optional<TypeOperationDTO> partialUpdate(TypeOperationDTO typeOperationDTO) {
        log.debug("Request to partially update TypeOperation : {}", typeOperationDTO);

        return typeOperationRepository
            .findById(typeOperationDTO.getId())
            .map(existingTypeOperation -> {
                typeOperationMapper.partialUpdate(existingTypeOperation, typeOperationDTO);

                return existingTypeOperation;
            })
            .map(typeOperationRepository::save)
            .map(savedTypeOperation -> {
                typeOperationSearchRepository.index(savedTypeOperation);
                return savedTypeOperation;
            })
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
        return typeOperationRepository.findById(id).map(typeOperationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeOperation : {}", id);
        typeOperationRepository.deleteById(id);
        typeOperationSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeOperationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TypeOperations for query {}", query);
        return typeOperationSearchRepository.search(query, pageable).map(typeOperationMapper::toDto);
    }
}
