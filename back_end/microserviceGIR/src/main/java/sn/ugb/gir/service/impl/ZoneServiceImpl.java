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
import sn.ugb.gir.service.ZoneService;
import sn.ugb.gir.service.dto.ZoneDTO;
import sn.ugb.gir.service.mapper.ZoneMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Zone}.
 */
@Service
@Transactional
public class ZoneServiceImpl implements ZoneService {

    private final Logger log = LoggerFactory.getLogger(ZoneServiceImpl.class);

    private final ZoneRepository zoneRepository;

    private final ZoneMapper zoneMapper;

    private static final String ENTITY_NAME = "microserviceGirZone";

    public ZoneServiceImpl(ZoneRepository zoneRepository, ZoneMapper zoneMapper) {
        this.zoneRepository = zoneRepository;
        this.zoneMapper = zoneMapper;
    }

    @Override
    public ZoneDTO save(ZoneDTO zoneDTO) {
        log.debug("Request to save Zone : {}", zoneDTO);
        validateData(zoneDTO);
        Zone zone = zoneMapper.toEntity(zoneDTO);
        zone = zoneRepository.save(zone);
        return zoneMapper.toDto(zone);
    }

    @Override
    public ZoneDTO update(ZoneDTO zoneDTO) {
        log.debug("Request to update Zone : {}", zoneDTO);
        validateData(zoneDTO);
        Zone zone = zoneMapper.toEntity(zoneDTO);
        zone = zoneRepository.save(zone);
        return zoneMapper.toDto(zone);
    }

    @Override
    public Optional<ZoneDTO> partialUpdate(ZoneDTO zoneDTO) {
        log.debug("Request to partially update Zone : {}", zoneDTO);
        validateData(zoneDTO);
        return zoneRepository
            .findById(zoneDTO.getId())
            .map(existingZone -> {
                zoneMapper.partialUpdate(existingZone, zoneDTO);

                return existingZone;
            })
            .map(zoneRepository::save)
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
    }

    private void validateData(ZoneDTO zoneDTO) {
        if (zoneDTO.getLibelleZone().isBlank()){
            throw new BadRequestAlertException("Le libellé ne peut pas être vide.", ENTITY_NAME, "libelleZoneNotNull");
        }
        Optional<Zone> existingZone = zoneRepository.findByLibelleZoneIgnoreCase(zoneDTO.getLibelleZone());
        if (existingZone.isPresent() && !existingZone.get().getId().equals(zoneDTO.getId())) {
            throw new BadRequestAlertException("Une zone avec le même libellé existe.", ENTITY_NAME, "libelleZoneExist");
        }
    }
}
