package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Universite;
import sn.ugb.gir.repository.UniversiteRepository;
import sn.ugb.gir.service.UniversiteService;
import sn.ugb.gir.service.dto.UniversiteDTO;
import sn.ugb.gir.service.mapper.UniversiteMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Universite}.
 */
@Service
@Transactional
public class UniversiteServiceImpl implements UniversiteService {

    private final Logger log = LoggerFactory.getLogger(UniversiteServiceImpl.class);

    private final UniversiteRepository universiteRepository;

    private final UniversiteMapper universiteMapper;

    public UniversiteServiceImpl(UniversiteRepository universiteRepository, UniversiteMapper universiteMapper) {
        this.universiteRepository = universiteRepository;
        this.universiteMapper = universiteMapper;
    }

    @Override
    public UniversiteDTO save(UniversiteDTO universiteDTO) {
        log.debug("Request to save Universite : {}", universiteDTO);
        Universite universite = universiteMapper.toEntity(universiteDTO);
        universite = universiteRepository.save(universite);
        return universiteMapper.toDto(universite);
    }

    @Override
    public UniversiteDTO update(UniversiteDTO universiteDTO) {
        log.debug("Request to update Universite : {}", universiteDTO);
        Universite universite = universiteMapper.toEntity(universiteDTO);
        universite = universiteRepository.save(universite);
        return universiteMapper.toDto(universite);
    }

    @Override
    public Optional<UniversiteDTO> partialUpdate(UniversiteDTO universiteDTO) {
        log.debug("Request to partially update Universite : {}", universiteDTO);

        return universiteRepository
            .findById(universiteDTO.getId())
            .map(existingUniversite -> {
                universiteMapper.partialUpdate(existingUniversite, universiteDTO);

                return existingUniversite;
            })
            .map(universiteRepository::save)
            .map(universiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UniversiteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Universites");
        return universiteRepository.findAll(pageable).map(universiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UniversiteDTO> findOne(Long id) {
        log.debug("Request to get Universite : {}", id);
        return universiteRepository.findById(id).map(universiteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Universite : {}", id);
        universiteRepository.deleteById(id);
    }
}
