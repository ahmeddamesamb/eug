package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.TypeFormation;
import sn.ugb.gir.repository.TypeFormationRepository;
import sn.ugb.gir.repository.search.TypeFormationSearchRepository;
import sn.ugb.gir.service.TypeFormationService;
import sn.ugb.gir.service.dto.TypeFormationDTO;
import sn.ugb.gir.service.mapper.TypeFormationMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeFormation}.
 */
@Service
@Transactional
public class TypeFormationServiceImpl implements TypeFormationService {

    private final Logger log = LoggerFactory.getLogger(TypeFormationServiceImpl.class);

    private final TypeFormationRepository typeFormationRepository;

    private final TypeFormationMapper typeFormationMapper;

    private final TypeFormationSearchRepository typeFormationSearchRepository;

    public TypeFormationServiceImpl(
        TypeFormationRepository typeFormationRepository,
        TypeFormationMapper typeFormationMapper,
        TypeFormationSearchRepository typeFormationSearchRepository
    ) {
        this.typeFormationRepository = typeFormationRepository;
        this.typeFormationMapper = typeFormationMapper;
        this.typeFormationSearchRepository = typeFormationSearchRepository;
    }

    @Override
    public TypeFormationDTO save(TypeFormationDTO typeFormationDTO) {
        log.debug("Request to save TypeFormation : {}", typeFormationDTO);
        TypeFormation typeFormation = typeFormationMapper.toEntity(typeFormationDTO);
        typeFormation = typeFormationRepository.save(typeFormation);
        TypeFormationDTO result = typeFormationMapper.toDto(typeFormation);
        typeFormationSearchRepository.index(typeFormation);
        return result;
    }

    @Override
    public TypeFormationDTO update(TypeFormationDTO typeFormationDTO) {
        log.debug("Request to update TypeFormation : {}", typeFormationDTO);
        TypeFormation typeFormation = typeFormationMapper.toEntity(typeFormationDTO);
        typeFormation = typeFormationRepository.save(typeFormation);
        TypeFormationDTO result = typeFormationMapper.toDto(typeFormation);
        typeFormationSearchRepository.index(typeFormation);
        return result;
    }

    @Override
    public Optional<TypeFormationDTO> partialUpdate(TypeFormationDTO typeFormationDTO) {
        log.debug("Request to partially update TypeFormation : {}", typeFormationDTO);

        return typeFormationRepository
            .findById(typeFormationDTO.getId())
            .map(existingTypeFormation -> {
                typeFormationMapper.partialUpdate(existingTypeFormation, typeFormationDTO);

                return existingTypeFormation;
            })
            .map(typeFormationRepository::save)
            .map(savedTypeFormation -> {
                typeFormationSearchRepository.index(savedTypeFormation);
                return savedTypeFormation;
            })
            .map(typeFormationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeFormationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeFormations");
        return typeFormationRepository.findAll(pageable).map(typeFormationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeFormationDTO> findOne(Long id) {
        log.debug("Request to get TypeFormation : {}", id);
        return typeFormationRepository.findById(id).map(typeFormationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeFormation : {}", id);
        typeFormationRepository.deleteById(id);
        typeFormationSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeFormationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TypeFormations for query {}", query);
        return typeFormationSearchRepository.search(query, pageable).map(typeFormationMapper::toDto);
    }
}
