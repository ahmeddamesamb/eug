package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.TypeSelection;
import sn.ugb.gir.repository.TypeSelectionRepository;
import sn.ugb.gir.service.TypeSelectionService;
import sn.ugb.gir.service.dto.TypeSelectionDTO;
import sn.ugb.gir.service.mapper.TypeSelectionMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeSelection}.
 */
@Service
@Transactional
public class TypeSelectionServiceImpl implements TypeSelectionService {

    private final Logger log = LoggerFactory.getLogger(TypeSelectionServiceImpl.class);

    private final TypeSelectionRepository typeSelectionRepository;

    private final TypeSelectionMapper typeSelectionMapper;

    public TypeSelectionServiceImpl(TypeSelectionRepository typeSelectionRepository, TypeSelectionMapper typeSelectionMapper) {
        this.typeSelectionRepository = typeSelectionRepository;
        this.typeSelectionMapper = typeSelectionMapper;
    }

    @Override
    public TypeSelectionDTO save(TypeSelectionDTO typeSelectionDTO) {
        log.debug("Request to save TypeSelection : {}", typeSelectionDTO);
        TypeSelection typeSelection = typeSelectionMapper.toEntity(typeSelectionDTO);
        typeSelection = typeSelectionRepository.save(typeSelection);
        return typeSelectionMapper.toDto(typeSelection);
    }

    @Override
    public TypeSelectionDTO update(TypeSelectionDTO typeSelectionDTO) {
        log.debug("Request to update TypeSelection : {}", typeSelectionDTO);
        TypeSelection typeSelection = typeSelectionMapper.toEntity(typeSelectionDTO);
        typeSelection = typeSelectionRepository.save(typeSelection);
        return typeSelectionMapper.toDto(typeSelection);
    }

    @Override
    public Optional<TypeSelectionDTO> partialUpdate(TypeSelectionDTO typeSelectionDTO) {
        log.debug("Request to partially update TypeSelection : {}", typeSelectionDTO);

        return typeSelectionRepository
            .findById(typeSelectionDTO.getId())
            .map(existingTypeSelection -> {
                typeSelectionMapper.partialUpdate(existingTypeSelection, typeSelectionDTO);

                return existingTypeSelection;
            })
            .map(typeSelectionRepository::save)
            .map(typeSelectionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeSelectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeSelections");
        return typeSelectionRepository.findAll(pageable).map(typeSelectionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeSelectionDTO> findOne(Long id) {
        log.debug("Request to get TypeSelection : {}", id);
        return typeSelectionRepository.findById(id).map(typeSelectionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeSelection : {}", id);
        typeSelectionRepository.deleteById(id);
    }
}
