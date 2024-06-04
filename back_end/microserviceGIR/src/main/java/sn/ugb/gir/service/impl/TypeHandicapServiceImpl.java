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

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeHandicap}.
 */
@Service
@Transactional
public class TypeHandicapServiceImpl implements TypeHandicapService {

    private final Logger log = LoggerFactory.getLogger(TypeHandicapServiceImpl.class);

    private final TypeHandicapRepository typeHandicapRepository;

    private final TypeHandicapMapper typeHandicapMapper;

    public TypeHandicapServiceImpl(TypeHandicapRepository typeHandicapRepository, TypeHandicapMapper typeHandicapMapper) {
        this.typeHandicapRepository = typeHandicapRepository;
        this.typeHandicapMapper = typeHandicapMapper;
    }

    @Override
    public TypeHandicapDTO save(TypeHandicapDTO typeHandicapDTO) {
        log.debug("Request to save TypeHandicap : {}", typeHandicapDTO);

        if(typeHandicapRepository.findByLibelleTypeHandicap(typeHandicapDTO.getLibelleTypeHandicap()).isPresent()){
            throw new BadRequestAlertException("A new niveau have an TypeHandicap exists ", ENTITY_NAME, "TypeHandicap exists");
        }

        TypeHandicap typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        return typeHandicapMapper.toDto(typeHandicap);
    }

    @Override
    public TypeHandicapDTO update(TypeHandicapDTO typeHandicapDTO) {
        log.debug("Request to update TypeHandicap : {}", typeHandicapDTO);

        if(typeHandicapRepository.findByLibelleTypeHandicap(typeHandicapDTO.getLibelleTypeHandicap()).isPresent()){
            throw new BadRequestAlertException("A new niveau have an TypeHandicap exists ", ENTITY_NAME, "TypeHandicap exists");
        }
        TypeHandicap typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        return typeHandicapMapper.toDto(typeHandicap);
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
    }
}
