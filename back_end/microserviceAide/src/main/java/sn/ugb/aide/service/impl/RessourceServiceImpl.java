package sn.ugb.aide.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.aide.domain.Ressource;
import sn.ugb.aide.repository.RessourceRepository;
import sn.ugb.aide.service.RessourceService;
import sn.ugb.aide.service.dto.RessourceDTO;
import sn.ugb.aide.service.mapper.RessourceMapper;

/**
 * Service Implementation for managing {@link sn.ugb.aide.domain.Ressource}.
 */
@Service
@Transactional
public class RessourceServiceImpl implements RessourceService {

    private final Logger log = LoggerFactory.getLogger(RessourceServiceImpl.class);

    private final RessourceRepository ressourceRepository;

    private final RessourceMapper ressourceMapper;

    public RessourceServiceImpl(RessourceRepository ressourceRepository, RessourceMapper ressourceMapper) {
        this.ressourceRepository = ressourceRepository;
        this.ressourceMapper = ressourceMapper;
    }

    @Override
    public RessourceDTO save(RessourceDTO ressourceDTO) {
        log.debug("Request to save Ressource : {}", ressourceDTO);
        Ressource ressource = ressourceMapper.toEntity(ressourceDTO);
        ressource = ressourceRepository.save(ressource);
        return ressourceMapper.toDto(ressource);
    }

    @Override
    public RessourceDTO update(RessourceDTO ressourceDTO) {
        log.debug("Request to update Ressource : {}", ressourceDTO);
        Ressource ressource = ressourceMapper.toEntity(ressourceDTO);
        ressource = ressourceRepository.save(ressource);
        return ressourceMapper.toDto(ressource);
    }

    @Override
    public Optional<RessourceDTO> partialUpdate(RessourceDTO ressourceDTO) {
        log.debug("Request to partially update Ressource : {}", ressourceDTO);

        return ressourceRepository
            .findById(ressourceDTO.getId())
            .map(existingRessource -> {
                ressourceMapper.partialUpdate(existingRessource, ressourceDTO);

                return existingRessource;
            })
            .map(ressourceRepository::save)
            .map(ressourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RessourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ressources");
        return ressourceRepository.findAll(pageable).map(ressourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RessourceDTO> findOne(Long id) {
        log.debug("Request to get Ressource : {}", id);
        return ressourceRepository.findById(id).map(ressourceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ressource : {}", id);
        ressourceRepository.deleteById(id);
    }
}
