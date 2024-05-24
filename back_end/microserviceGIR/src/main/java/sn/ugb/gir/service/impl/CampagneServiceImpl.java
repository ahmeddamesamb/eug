package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Campagne;
import sn.ugb.gir.repository.CampagneRepository;
import sn.ugb.gir.service.CampagneService;
import sn.ugb.gir.service.dto.CampagneDTO;
import sn.ugb.gir.service.mapper.CampagneMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Campagne}.
 */
@Service
@Transactional
public class CampagneServiceImpl implements CampagneService {

    private final Logger log = LoggerFactory.getLogger(CampagneServiceImpl.class);

    private final CampagneRepository campagneRepository;

    private final CampagneMapper campagneMapper;

    public CampagneServiceImpl(CampagneRepository campagneRepository, CampagneMapper campagneMapper) {
        this.campagneRepository = campagneRepository;
        this.campagneMapper = campagneMapper;
    }

    @Override
    public CampagneDTO save(CampagneDTO campagneDTO) {
        log.debug("Request to save Campagne : {}", campagneDTO);
        Campagne campagne = campagneMapper.toEntity(campagneDTO);
        campagne = campagneRepository.save(campagne);
        return campagneMapper.toDto(campagne);
    }

    @Override
    public CampagneDTO update(CampagneDTO campagneDTO) {
        log.debug("Request to update Campagne : {}", campagneDTO);
        Campagne campagne = campagneMapper.toEntity(campagneDTO);
        campagne = campagneRepository.save(campagne);
        return campagneMapper.toDto(campagne);
    }

    @Override
    public Optional<CampagneDTO> partialUpdate(CampagneDTO campagneDTO) {
        log.debug("Request to partially update Campagne : {}", campagneDTO);

        return campagneRepository
            .findById(campagneDTO.getId())
            .map(existingCampagne -> {
                campagneMapper.partialUpdate(existingCampagne, campagneDTO);

                return existingCampagne;
            })
            .map(campagneRepository::save)
            .map(campagneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CampagneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Campagnes");
        return campagneRepository.findAll(pageable).map(campagneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CampagneDTO> findOne(Long id) {
        log.debug("Request to get Campagne : {}", id);
        return campagneRepository.findById(id).map(campagneMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Campagne : {}", id);
        campagneRepository.deleteById(id);
    }
}
