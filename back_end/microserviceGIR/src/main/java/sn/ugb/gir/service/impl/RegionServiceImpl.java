package sn.ugb.gir.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Region;
import sn.ugb.gir.repository.RegionRepository;
import sn.ugb.gir.service.RegionService;
import sn.ugb.gir.service.dto.RegionDTO;
import sn.ugb.gir.service.mapper.RegionMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import java.util.Optional;

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
    Region region = regionMapper.toEntity(regionDTO);
    if (region.getLibelleRegion()==null || region.getLibelleRegion().trim().isEmpty()){
        throw new BadRequestAlertException("Le libellé Region est obligatoire", "region", "libelleRegionNull");
    }

    if (regionRepository.findByLibelleRegion(region.getLibelleRegion()).isPresent()) {
        throw new BadRequestAlertException("Une Region avec ce libellé existe déjà", "region", "libelleRegionExists");
    }
    region = regionRepository.save(region);
    return regionMapper.toDto(region);
}

@Override
public RegionDTO update(RegionDTO regionDTO) {
    log.debug("Request to update Region : {}", regionDTO);
    Region region = regionMapper.toEntity(regionDTO);
    if (region.getLibelleRegion()==null || region.getLibelleRegion().trim().isEmpty()){
        throw new BadRequestAlertException("Le libellé Region est obligatoire", "region", "libelleRegionNull");
    }
    if (regionRepository.findByLibelleRegion(region.getLibelleRegion()).isPresent()) {
        throw new BadRequestAlertException("Une Region avec ce libellé existe déjà", "region", "libelleRegionExists");
    }
    region = regionRepository.save(region);
    return regionMapper.toDto(region);
}

@Override
public Optional<RegionDTO> partialUpdate(RegionDTO regionDTO) {
    log.debug("Request to partially update Region : {}", regionDTO);
    if (regionDTO.getLibelleRegion()==null || regionDTO.getLibelleRegion().trim().isEmpty()){
        throw new BadRequestAlertException("Le libellé Region est obligatoire", "region", "libelleRegionNull");
    }
    return regionRepository
               .findById(regionDTO.getId())
               .map(existingTypeFrais -> {
                   regionRepository.findByLibelleRegion(regionDTO.getLibelleRegion()).ifPresent(existing -> {
                       if (!existing.getId().equals(existingTypeFrais.getId())) {
                           throw new BadRequestAlertException("Une Region avec ce libellé existe déjà", "region", "libelleRegionExists");
                       }
                   });

                   regionMapper.partialUpdate(existingTypeFrais, regionDTO);
                   return existingTypeFrais;
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
//************************************************ FIND ALL REGIONS BY PAYS ID **************************************

@Override
public Page<RegionDTO> findAllRegionByPays(Long paysId, Pageable pageable) {
    return regionRepository.findByPaysId(paysId, pageable).map(regionMapper::toDto);
}


}
