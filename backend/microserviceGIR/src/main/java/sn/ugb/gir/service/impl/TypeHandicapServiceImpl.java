package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.TypeHandicap;
import sn.ugb.gir.repository.TypeHandicapRepository;
import sn.ugb.gir.repository.search.TypeHandicapSearchRepository;
import sn.ugb.gir.service.TypeHandicapService;
import sn.ugb.gir.service.dto.TypeHandicapDTO;
import sn.ugb.gir.service.mapper.TypeHandicapMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeHandicap}.
 */
@Service
@Transactional
public class TypeHandicapServiceImpl implements TypeHandicapService {

    private final Logger log = LoggerFactory.getLogger(TypeHandicapServiceImpl.class);

    private final TypeHandicapRepository typeHandicapRepository;

    private final TypeHandicapMapper typeHandicapMapper;

    private final TypeHandicapSearchRepository typeHandicapSearchRepository;

    public TypeHandicapServiceImpl(
        TypeHandicapRepository typeHandicapRepository,
        TypeHandicapMapper typeHandicapMapper,
        TypeHandicapSearchRepository typeHandicapSearchRepository
    ) {
        this.typeHandicapRepository = typeHandicapRepository;
        this.typeHandicapMapper = typeHandicapMapper;
        this.typeHandicapSearchRepository = typeHandicapSearchRepository;
    }

    @Override
    public TypeHandicapDTO save(TypeHandicapDTO typeHandicapDTO) {
        log.debug("Request to save TypeHandicap : {}", typeHandicapDTO);
        TypeHandicap typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        TypeHandicapDTO result = typeHandicapMapper.toDto(typeHandicap);
        typeHandicapSearchRepository.index(typeHandicap);
        return result;
    }

    @Override
    public TypeHandicapDTO update(TypeHandicapDTO typeHandicapDTO) {
        log.debug("Request to update TypeHandicap : {}", typeHandicapDTO);
        TypeHandicap typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        TypeHandicapDTO result = typeHandicapMapper.toDto(typeHandicap);
        typeHandicapSearchRepository.index(typeHandicap);
        return result;
    }

    @Override
    public Optional<TypeHandicapDTO> partialUpdate(TypeHandicapDTO typeHandicapDTO) {
        log.debug("Request to partially update TypeHandicap : {}", typeHandicapDTO);

        return typeHandicapRepository
            .findById(typeHandicapDTO.getId())
            .map(existingTypeHandicap -> {
                typeHandicapMapper.partialUpdate(existingTypeHandicap, typeHandicapDTO);

                return existingTypeHandicap;
            })
            .map(typeHandicapRepository::save)
            .map(savedTypeHandicap -> {
                typeHandicapSearchRepository.index(savedTypeHandicap);
                return savedTypeHandicap;
            })
            .map(typeHandicapMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeHandicapDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeHandicaps");
        return typeHandicapRepository.findAll(pageable).map(typeHandicapMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeHandicapDTO> findOne(Long id) {
        log.debug("Request to get TypeHandicap : {}", id);
        return typeHandicapRepository.findById(id).map(typeHandicapMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeHandicap : {}", id);
        typeHandicapRepository.deleteById(id);
        typeHandicapSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeHandicapDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TypeHandicaps for query {}", query);
        return typeHandicapSearchRepository.search(query, pageable).map(typeHandicapMapper::toDto);
    }
}
