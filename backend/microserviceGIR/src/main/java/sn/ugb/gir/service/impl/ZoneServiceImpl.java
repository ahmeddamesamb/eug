package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Zone;
import sn.ugb.gir.repository.ZoneRepository;
import sn.ugb.gir.repository.search.ZoneSearchRepository;
import sn.ugb.gir.service.ZoneService;
import sn.ugb.gir.service.dto.ZoneDTO;
import sn.ugb.gir.service.mapper.ZoneMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Zone}.
 */
@Service
@Transactional
public class ZoneServiceImpl implements ZoneService {

    private final Logger log = LoggerFactory.getLogger(ZoneServiceImpl.class);

    private final ZoneRepository zoneRepository;

    private final ZoneMapper zoneMapper;

    private final ZoneSearchRepository zoneSearchRepository;

    public ZoneServiceImpl(ZoneRepository zoneRepository, ZoneMapper zoneMapper, ZoneSearchRepository zoneSearchRepository) {
        this.zoneRepository = zoneRepository;
        this.zoneMapper = zoneMapper;
        this.zoneSearchRepository = zoneSearchRepository;
    }

    @Override
    public ZoneDTO save(ZoneDTO zoneDTO) {
        log.debug("Request to save Zone : {}", zoneDTO);
        Zone zone = zoneMapper.toEntity(zoneDTO);
        zone = zoneRepository.save(zone);
        ZoneDTO result = zoneMapper.toDto(zone);
        zoneSearchRepository.index(zone);
        return result;
    }

    @Override
    public ZoneDTO update(ZoneDTO zoneDTO) {
        log.debug("Request to update Zone : {}", zoneDTO);
        Zone zone = zoneMapper.toEntity(zoneDTO);
        zone = zoneRepository.save(zone);
        ZoneDTO result = zoneMapper.toDto(zone);
        zoneSearchRepository.index(zone);
        return result;
    }

    @Override
    public Optional<ZoneDTO> partialUpdate(ZoneDTO zoneDTO) {
        log.debug("Request to partially update Zone : {}", zoneDTO);

        return zoneRepository
            .findById(zoneDTO.getId())
            .map(existingZone -> {
                zoneMapper.partialUpdate(existingZone, zoneDTO);

                return existingZone;
            })
            .map(zoneRepository::save)
            .map(savedZone -> {
                zoneSearchRepository.index(savedZone);
                return savedZone;
            })
            .map(zoneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ZoneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Zones");
        return zoneRepository.findAll(pageable).map(zoneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ZoneDTO> findOne(Long id) {
        log.debug("Request to get Zone : {}", id);
        return zoneRepository.findById(id).map(zoneMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Zone : {}", id);
        zoneRepository.deleteById(id);
        zoneSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ZoneDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Zones for query {}", query);
        return zoneSearchRepository.search(query, pageable).map(zoneMapper::toDto);
    }
}
