package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.TypeFrais;
import sn.ugb.gir.repository.TypeFraisRepository;
import sn.ugb.gir.repository.search.TypeFraisSearchRepository;
import sn.ugb.gir.service.TypeFraisService;
import sn.ugb.gir.service.dto.TypeFraisDTO;
import sn.ugb.gir.service.mapper.TypeFraisMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeFrais}.
 */
@Service
@Transactional
public class TypeFraisServiceImpl implements TypeFraisService {

    private final Logger log = LoggerFactory.getLogger(TypeFraisServiceImpl.class);

    private final TypeFraisRepository typeFraisRepository;

    private final TypeFraisMapper typeFraisMapper;

    private final TypeFraisSearchRepository typeFraisSearchRepository;

    public TypeFraisServiceImpl(
        TypeFraisRepository typeFraisRepository,
        TypeFraisMapper typeFraisMapper,
        TypeFraisSearchRepository typeFraisSearchRepository
    ) {
        this.typeFraisRepository = typeFraisRepository;
        this.typeFraisMapper = typeFraisMapper;
        this.typeFraisSearchRepository = typeFraisSearchRepository;
    }

    @Override
    public TypeFraisDTO save(TypeFraisDTO typeFraisDTO) {
        log.debug("Request to save TypeFrais : {}", typeFraisDTO);
        TypeFrais typeFrais = typeFraisMapper.toEntity(typeFraisDTO);
        typeFrais = typeFraisRepository.save(typeFrais);
        TypeFraisDTO result = typeFraisMapper.toDto(typeFrais);
        typeFraisSearchRepository.index(typeFrais);
        return result;
    }

    @Override
    public TypeFraisDTO update(TypeFraisDTO typeFraisDTO) {
        log.debug("Request to update TypeFrais : {}", typeFraisDTO);
        TypeFrais typeFrais = typeFraisMapper.toEntity(typeFraisDTO);
        typeFrais = typeFraisRepository.save(typeFrais);
        TypeFraisDTO result = typeFraisMapper.toDto(typeFrais);
        typeFraisSearchRepository.index(typeFrais);
        return result;
    }

    @Override
    public Optional<TypeFraisDTO> partialUpdate(TypeFraisDTO typeFraisDTO) {
        log.debug("Request to partially update TypeFrais : {}", typeFraisDTO);

        return typeFraisRepository
            .findById(typeFraisDTO.getId())
            .map(existingTypeFrais -> {
                typeFraisMapper.partialUpdate(existingTypeFrais, typeFraisDTO);

                return existingTypeFrais;
            })
            .map(typeFraisRepository::save)
            .map(savedTypeFrais -> {
                typeFraisSearchRepository.index(savedTypeFrais);
                return savedTypeFrais;
            })
            .map(typeFraisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeFraisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeFrais");
        return typeFraisRepository.findAll(pageable).map(typeFraisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeFraisDTO> findOne(Long id) {
        log.debug("Request to get TypeFrais : {}", id);
        return typeFraisRepository.findById(id).map(typeFraisMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeFrais : {}", id);
        typeFraisRepository.deleteById(id);
        typeFraisSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeFraisDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TypeFrais for query {}", query);
        return typeFraisSearchRepository.search(query, pageable).map(typeFraisMapper::toDto);
    }
}
