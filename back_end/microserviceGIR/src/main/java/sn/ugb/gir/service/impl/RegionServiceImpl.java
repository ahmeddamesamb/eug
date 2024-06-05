package sn.ugb.gir.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Pays;
import sn.ugb.gir.domain.Region;
import sn.ugb.gir.repository.RegionRepository;
import sn.ugb.gir.service.RegionService;
import sn.ugb.gir.service.dto.PaysDTO;
import sn.ugb.gir.service.dto.RegionDTO;
import sn.ugb.gir.service.mapper.RegionMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import java.util.Optional;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Region}.
 */
@Service
@Transactional
public class RegionServiceImpl implements RegionService {

private final Logger log = LoggerFactory.getLogger(RegionServiceImpl.class);

private final RegionRepository regionRepository;

private final RegionMapper regionMapper;

public RegionServiceImpl(RegionRepository regionRepository, RegionMapper regionMapper) {
    this.regionRepository = regionRepository;
    this.regionMapper = regionMapper;
}

@Override
public RegionDTO save(RegionDTO regionDTO) {
    log.debug("Request to save Region : {}", regionDTO);
    validateData(regionDTO);
    Region region = regionMapper.toEntity(regionDTO);
    region = regionRepository.save(region);
    return regionMapper.toDto(region);
}

@Override
public RegionDTO update(RegionDTO regionDTO) {
    log.debug("Request to update Region : {}", regionDTO);
    validateData(regionDTO);
    Region region = regionMapper.toEntity(regionDTO);
    region = regionRepository.save(region);
    return regionMapper.toDto(region);
}

@Override
public Optional<RegionDTO> partialUpdate(RegionDTO regionDTO) {
    log.debug("Request to partially update Region : {}", regionDTO);
    validateData(regionDTO);
    return regionRepository
               .findById(regionDTO.getId())
               .map(existingRegion -> {
                   regionRepository.findByLibelleRegionIgnoreCase(regionDTO.getLibelleRegion()).ifPresent(existing -> {
                       if (!existing.getId().equals(existingRegion.getId())) {
                           throw new BadRequestAlertException("Une Region avec ce libellé existe déjà", "region", "libelleRegionExists");
                       }
                   });

                   regionMapper.partialUpdate(existingRegion, regionDTO);
                   return existingRegion;
               })
               .map(regionRepository::save)
               .map(regionMapper::toDto);
}

@Override
@Transactional(readOnly = true)
public Page<RegionDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Regions");
    return regionRepository.findAll(pageable).map(regionMapper::toDto);
}

@Override
@Transactional(readOnly = true)
public Optional<RegionDTO> findOne(Long id) {
    log.debug("Request to get Region : {}", id);
    return regionRepository.findById(id).map(regionMapper::toDto);
}

@Override
public void delete(Long id) {
    log.debug("Request to delete Region : {}", id);
    regionRepository.deleteById(id);
}
@Override
public Page<RegionDTO> findAllRegionByPays(Long paysId, Pageable pageable) {
    return regionRepository.findByPaysId(paysId, pageable).map(regionMapper::toDto);
}

private void validateData(RegionDTO  regionDTO) {
    if (regionDTO.getLibelleRegion().isEmpty() || regionDTO.getLibelleRegion().isBlank()){
        throw new BadRequestAlertException("Le Region ne peut pas être vide.", ENTITY_NAME, "getLibelleRegionNotNull");
    }
    Optional<Region> existingRegion = regionRepository.findByLibelleRegionIgnoreCase(regionDTO.getLibelleRegion());
    if (existingRegion.isPresent() && !existingRegion.get().getId().equals(regionDTO.getId())) {
        throw new BadRequestAlertException("Un Region avec le même libellé existe.", ENTITY_NAME, "getLibelleRegionExist");
    }
}
}
