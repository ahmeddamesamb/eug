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
import sn.ugb.gir.service.TypeFraisService;
import sn.ugb.gir.service.dto.TypeFraisDTO;
import sn.ugb.gir.service.mapper.TypeFraisMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.TypeFrais}.
 */
@Service
@Transactional
public class TypeFraisServiceImpl implements TypeFraisService {

    private final Logger log = LoggerFactory.getLogger(TypeFraisServiceImpl.class);

    private final TypeFraisRepository typeFraisRepository;

    private final TypeFraisMapper typeFraisMapper;

    public TypeFraisServiceImpl(TypeFraisRepository typeFraisRepository, TypeFraisMapper typeFraisMapper) {
        this.typeFraisRepository = typeFraisRepository;
        this.typeFraisMapper = typeFraisMapper;
    }

    @Override
    public TypeFraisDTO save(TypeFraisDTO typeFraisDTO) {
        log.debug("Request to save TypeFrais : {}", typeFraisDTO);
        TypeFrais typeFrais = typeFraisMapper.toEntity(typeFraisDTO);
        if (typeFrais.getLibelleTypeFrais()==null || typeFrais.getLibelleTypeFrais().trim().isEmpty()){
            throw new BadRequestAlertException("Le libellé typeFrais est obligatoire", "typeFrais", "libelleNull");
        }

        if (typeFraisRepository.findByLibelleTypeFrais(typeFrais.getLibelleTypeFrais()).isPresent()) {
            throw new BadRequestAlertException("Un type de frais avec ce libellé existe déjà", "typeFrais", "libelleExists");
        }
        typeFrais = typeFraisRepository.save(typeFrais);
        return typeFraisMapper.toDto(typeFrais);
    }

    @Override
    public TypeFraisDTO update(TypeFraisDTO typeFraisDTO) {
        log.debug("Request to update TypeFrais : {}", typeFraisDTO);
        TypeFrais typeFrais = typeFraisMapper.toEntity(typeFraisDTO);
        if (typeFrais.getLibelleTypeFrais()==null || typeFrais.getLibelleTypeFrais().trim().isEmpty()){
            throw new BadRequestAlertException("Le libellé typeFrais est obligatoire", "typeFrais", "libelleNull");
        }
        if (typeFraisRepository.findByLibelleTypeFrais(typeFrais.getLibelleTypeFrais()).isPresent()) {
            throw new BadRequestAlertException("Un type de frais avec ce libellé existe déjà", "typeFrais", "libelleExists");
        }
        typeFrais = typeFraisRepository.save(typeFrais);
        return typeFraisMapper.toDto(typeFrais);
    }

    @Override
    public Optional<TypeFraisDTO> partialUpdate(TypeFraisDTO typeFraisDTO) {
        log.debug("Request to partially update TypeFrais : {}", typeFraisDTO);

        if (typeFraisDTO.getLibelleTypeFrais()==null || typeFraisDTO.getLibelleTypeFrais().trim().isEmpty()){
            throw new BadRequestAlertException("Le libellé typeFrais est obligatoire", "typeFrais", "libelleNull");
        }
        return typeFraisRepository
                   .findById(typeFraisDTO.getId())
                   .map(existingTypeFrais -> {
                       typeFraisRepository.findByLibelleTypeFrais(typeFraisDTO.getLibelleTypeFrais()).ifPresent(existing -> {
                           if (!existing.getId().equals(existingTypeFrais.getId())) {
                               throw new BadRequestAlertException("Un type de frais avec ce libellé existe déjà", "typeFrais", "libelletypeFraisExists");
                           }
                       });

                       typeFraisMapper.partialUpdate(existingTypeFrais, typeFraisDTO);
                       return existingTypeFrais;
                   })
                   .map(typeFraisRepository::save)
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
    }
}
