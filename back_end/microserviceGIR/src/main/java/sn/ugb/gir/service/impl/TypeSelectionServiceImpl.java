package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Serie;
import sn.ugb.gir.domain.TypeSelection;
import sn.ugb.gir.repository.TypeSelectionRepository;
import sn.ugb.gir.service.TypeSelectionService;
import sn.ugb.gir.service.dto.SerieDTO;
import sn.ugb.gir.service.dto.TypeSelectionDTO;
import sn.ugb.gir.service.mapper.TypeSelectionMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeSelection}.
 */
@Service
@Transactional
public class TypeSelectionServiceImpl implements TypeSelectionService {

    private final Logger log = LoggerFactory.getLogger(TypeSelectionServiceImpl.class);

    private final TypeSelectionRepository typeSelectionRepository;

    private final TypeSelectionMapper typeSelectionMapper;

    private static final String ENTITY_NAME = "microserviceGirTypeSelection";

    public TypeSelectionServiceImpl(TypeSelectionRepository typeSelectionRepository, TypeSelectionMapper typeSelectionMapper) {
        this.typeSelectionRepository = typeSelectionRepository;
        this.typeSelectionMapper = typeSelectionMapper;
    }

    @Override
    public TypeSelectionDTO save(TypeSelectionDTO typeSelectionDTO) {
        log.debug("Request to save TypeSelection : {}", typeSelectionDTO);
        validateData(typeSelectionDTO);
        TypeSelection typeSelection = typeSelectionMapper.toEntity(typeSelectionDTO);
        typeSelection = typeSelectionRepository.save(typeSelection);
        return typeSelectionMapper.toDto(typeSelection);
    }

    @Override
    public TypeSelectionDTO update(TypeSelectionDTO typeSelectionDTO) {
        log.debug("Request to update TypeSelection : {}", typeSelectionDTO);
        validateData(typeSelectionDTO);
        TypeSelection typeSelection = typeSelectionMapper.toEntity(typeSelectionDTO);
        typeSelection = typeSelectionRepository.save(typeSelection);
        return typeSelectionMapper.toDto(typeSelection);
    }

    @Override
    public Optional<TypeSelectionDTO> partialUpdate(TypeSelectionDTO typeSelectionDTO) {
        log.debug("Request to partially update TypeSelection : {}", typeSelectionDTO);
        validateData(typeSelectionDTO);
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

    private void validateData(TypeSelectionDTO typeSelectionDTO) {
        if (typeSelectionDTO.getLibelleTypeSelection().isEmpty() || typeSelectionDTO.getLibelleTypeSelection().isBlank()){
            throw new BadRequestAlertException("Le libellé ne peut pas être vide.", ENTITY_NAME, "libelleTypeselectionNotNull");
        }
        Optional<TypeSelection> existingTypeSelection = typeSelectionRepository.findByLibelleTypeSelection(typeSelectionDTO.getLibelleTypeSelection());
        if (existingTypeSelection.isPresent() && !existingTypeSelection.get().getId().equals(typeSelectionDTO.getId())) {
            throw new BadRequestAlertException("Un type selection avec le même libellé existe.", ENTITY_NAME, "libelleTypeSelectionExist");
        }
    }
}
