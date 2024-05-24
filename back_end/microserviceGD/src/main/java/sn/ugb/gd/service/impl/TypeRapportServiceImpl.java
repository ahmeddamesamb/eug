package sn.ugb.gd.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gd.domain.TypeRapport;
import sn.ugb.gd.repository.TypeRapportRepository;
import sn.ugb.gd.service.TypeRapportService;
import sn.ugb.gd.service.dto.TypeRapportDTO;
import sn.ugb.gd.service.mapper.TypeRapportMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gd.domain.TypeRapport}.
 */
@Service
@Transactional
public class TypeRapportServiceImpl implements TypeRapportService {

    private final Logger log = LoggerFactory.getLogger(TypeRapportServiceImpl.class);

    private final TypeRapportRepository typeRapportRepository;

    private final TypeRapportMapper typeRapportMapper;

    public TypeRapportServiceImpl(TypeRapportRepository typeRapportRepository, TypeRapportMapper typeRapportMapper) {
        this.typeRapportRepository = typeRapportRepository;
        this.typeRapportMapper = typeRapportMapper;
    }

    @Override
    public TypeRapportDTO save(TypeRapportDTO typeRapportDTO) {
        log.debug("Request to save TypeRapport : {}", typeRapportDTO);
        TypeRapport typeRapport = typeRapportMapper.toEntity(typeRapportDTO);
        typeRapport = typeRapportRepository.save(typeRapport);
        return typeRapportMapper.toDto(typeRapport);
    }

    @Override
    public TypeRapportDTO update(TypeRapportDTO typeRapportDTO) {
        log.debug("Request to update TypeRapport : {}", typeRapportDTO);
        TypeRapport typeRapport = typeRapportMapper.toEntity(typeRapportDTO);
        typeRapport = typeRapportRepository.save(typeRapport);
        return typeRapportMapper.toDto(typeRapport);
    }

    @Override
    public Optional<TypeRapportDTO> partialUpdate(TypeRapportDTO typeRapportDTO) {
        log.debug("Request to partially update TypeRapport : {}", typeRapportDTO);

        return typeRapportRepository
            .findById(typeRapportDTO.getId())
            .map(existingTypeRapport -> {
                typeRapportMapper.partialUpdate(existingTypeRapport, typeRapportDTO);

                return existingTypeRapport;
            })
            .map(typeRapportRepository::save)
            .map(typeRapportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeRapportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeRapports");
        return typeRapportRepository.findAll(pageable).map(typeRapportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeRapportDTO> findOne(Long id) {
        log.debug("Request to get TypeRapport : {}", id);
        return typeRapportRepository.findById(id).map(typeRapportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeRapport : {}", id);
        typeRapportRepository.deleteById(id);
    }
}
