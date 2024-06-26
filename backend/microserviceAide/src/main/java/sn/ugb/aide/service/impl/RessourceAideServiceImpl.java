package sn.ugb.aide.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.aide.domain.RessourceAide;
import sn.ugb.aide.repository.RessourceAideRepository;
import sn.ugb.aide.repository.search.RessourceAideSearchRepository;
import sn.ugb.aide.service.RessourceAideService;
import sn.ugb.aide.service.dto.RessourceAideDTO;
import sn.ugb.aide.service.mapper.RessourceAideMapper;

/**
 * Service Implementation for managing {@link sn.ugb.aide.domain.RessourceAide}.
 */
@Service
@Transactional
public class RessourceAideServiceImpl implements RessourceAideService {

    private final Logger log = LoggerFactory.getLogger(RessourceAideServiceImpl.class);

    private final RessourceAideRepository ressourceAideRepository;

    private final RessourceAideMapper ressourceAideMapper;

    private final RessourceAideSearchRepository ressourceAideSearchRepository;

    public RessourceAideServiceImpl(
        RessourceAideRepository ressourceAideRepository,
        RessourceAideMapper ressourceAideMapper,
        RessourceAideSearchRepository ressourceAideSearchRepository
    ) {
        this.ressourceAideRepository = ressourceAideRepository;
        this.ressourceAideMapper = ressourceAideMapper;
        this.ressourceAideSearchRepository = ressourceAideSearchRepository;
    }

    @Override
    public RessourceAideDTO save(RessourceAideDTO ressourceAideDTO) {
        log.debug("Request to save RessourceAide : {}", ressourceAideDTO);
        RessourceAide ressourceAide = ressourceAideMapper.toEntity(ressourceAideDTO);
        ressourceAide = ressourceAideRepository.save(ressourceAide);
        RessourceAideDTO result = ressourceAideMapper.toDto(ressourceAide);
        ressourceAideSearchRepository.index(ressourceAide);
        return result;
    }

    @Override
    public RessourceAideDTO update(RessourceAideDTO ressourceAideDTO) {
        log.debug("Request to update RessourceAide : {}", ressourceAideDTO);
        RessourceAide ressourceAide = ressourceAideMapper.toEntity(ressourceAideDTO);
        ressourceAide = ressourceAideRepository.save(ressourceAide);
        RessourceAideDTO result = ressourceAideMapper.toDto(ressourceAide);
        ressourceAideSearchRepository.index(ressourceAide);
        return result;
    }

    @Override
    public Optional<RessourceAideDTO> partialUpdate(RessourceAideDTO ressourceAideDTO) {
        log.debug("Request to partially update RessourceAide : {}", ressourceAideDTO);

        return ressourceAideRepository
            .findById(ressourceAideDTO.getId())
            .map(existingRessourceAide -> {
                ressourceAideMapper.partialUpdate(existingRessourceAide, ressourceAideDTO);

                return existingRessourceAide;
            })
            .map(ressourceAideRepository::save)
            .map(savedRessourceAide -> {
                ressourceAideSearchRepository.index(savedRessourceAide);
                return savedRessourceAide;
            })
            .map(ressourceAideMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RessourceAideDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RessourceAides");
        return ressourceAideRepository.findAll(pageable).map(ressourceAideMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RessourceAideDTO> findOne(Long id) {
        log.debug("Request to get RessourceAide : {}", id);
        return ressourceAideRepository.findById(id).map(ressourceAideMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RessourceAide : {}", id);
        ressourceAideRepository.deleteById(id);
        ressourceAideSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RessourceAideDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RessourceAides for query {}", query);
        return ressourceAideSearchRepository.search(query, pageable).map(ressourceAideMapper::toDto);
    }
}
