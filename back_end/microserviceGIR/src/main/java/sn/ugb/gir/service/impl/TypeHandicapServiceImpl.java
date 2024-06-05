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
import sn.ugb.gir.service.TypeHandicapService;
import sn.ugb.gir.service.dto.TypeHandicapDTO;
import sn.ugb.gir.service.mapper.TypeHandicapMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;


/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeHandicap}.
 */
@Service
@Transactional
public class TypeHandicapServiceImpl implements TypeHandicapService {

    private final Logger log = LoggerFactory.getLogger(TypeHandicapServiceImpl.class);
    private static final String ENTITY_NAME = "typeHandicap";

    private final TypeHandicapRepository typeHandicapRepository;
    private final TypeHandicapMapper typeHandicapMapper;

    public TypeHandicapServiceImpl(TypeHandicapRepository typeHandicapRepository, TypeHandicapMapper typeHandicapMapper) {
        this.typeHandicapRepository = typeHandicapRepository;
        this.typeHandicapMapper = typeHandicapMapper;
    }

    @Override
    public TypeHandicapDTO save(TypeHandicapDTO typeHandicapDTO) {
        log.debug("Request to save TypeHandicap : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeHandicap(), typeHandicapDTO.getId());

        TypeHandicap typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        return typeHandicapMapper.toDto(typeHandicap);
    }

    @Override
    public TypeHandicapDTO update(TypeHandicapDTO typeHandicapDTO) {
        log.debug("Request to update TypeHandicap : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeHandicap(), typeHandicapDTO.getId());

        TypeHandicap typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        return typeHandicapMapper.toDto(typeHandicap);
    }

    @Override
    public Optional<TypeHandicapDTO> partialUpdate(TypeHandicapDTO typeHandicapDTO) {
        log.debug("Request to partially update TypeHandicap : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeHandicap(), typeHandicapDTO.getId());

        return typeHandicapRepository
            .findById(typeHandicapDTO.getId())
            .map(existingTypeHandicap -> {
                typeHandicapMapper.partialUpdate(existingTypeHandicap, typeHandicapDTO);
                return existingTypeHandicap;
            })
            .map(typeHandicapRepository::save)
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
        return typeHandicapRepository.findById(id)
            .map(typeHandicapMapper::toDto)
            .or(() -> {
                log.warn("TypeHandicap with id {} not found", id);
                return Optional.empty();
            });
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeHandicap : {}", id);

        if (!typeHandicapRepository.existsById(id)) {
            throw new BadRequestAlertException("TypeHandicap with id " + id + " not found", ENTITY_NAME, "typeHandicapNotFound");
        }

        typeHandicapRepository.deleteById(id);
    }

    private void validateData(String libelleTypeHandicap, Long id) {
        if (libelleTypeHandicap == null || libelleTypeHandicap.trim().isBlank()) {
            throw new BadRequestAlertException("TypeHandicap label cannot be empty", ENTITY_NAME, "emptyLibelleTypeHandicap");
        }

        Optional<TypeHandicap> existingTypeHandicap = typeHandicapRepository.findByLibelleTypeHandicapIgnoreCase(libelleTypeHandicap.trim());
        if (existingTypeHandicap.isPresent() && !existingTypeHandicap.get().getId().equals(id)) {
            throw new BadRequestAlertException("A TypeHandicap with the same name already exists", ENTITY_NAME, "typeHandicapExists");
        }
    }

}
