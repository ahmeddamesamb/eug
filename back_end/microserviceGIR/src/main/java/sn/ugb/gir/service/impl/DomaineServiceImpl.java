package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Domaine;
import sn.ugb.gir.repository.DomaineRepository;
import sn.ugb.gir.service.DomaineService;
import sn.ugb.gir.service.dto.DomaineDTO;
import sn.ugb.gir.service.mapper.DomaineMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Domaine}.
 */
@Service
@Transactional
public class DomaineServiceImpl implements DomaineService {

    private final Logger log = LoggerFactory.getLogger(DomaineServiceImpl.class);

    private final DomaineRepository domaineRepository;

    private final DomaineMapper domaineMapper;

    public DomaineServiceImpl(DomaineRepository domaineRepository, DomaineMapper domaineMapper) {
        this.domaineRepository = domaineRepository;
        this.domaineMapper = domaineMapper;
    }

    @Override
    public DomaineDTO save(DomaineDTO domaineDTO) {
        log.debug("Request to save Domaine : {}", domaineDTO);
        Domaine domaine = domaineMapper.toEntity(domaineDTO);

        if (domaine.getLibelleDomaine()==null || domaine.getLibelleDomaine().trim().isEmpty()){
            throw new BadRequestAlertException("Le  LibelleDomaine est obligatoire", "LibelleDomaine", "LibelleDomaineNull");
        }

        if (domaineRepository.findByLibelleDomaine(domaine.getLibelleDomaine()).isPresent()) {
            throw new BadRequestAlertException("Une LibelleDomaine avec ce libellé existe déjà", "LibelleDomaine", "LibelleDomaineRegionExists");
        }
        domaine = domaineRepository.save(domaine);
        return domaineMapper.toDto(domaine);
    }

    @Override
    public DomaineDTO update(DomaineDTO domaineDTO) {
        log.debug("Request to update Domaine : {}", domaineDTO);
        Domaine domaine = domaineMapper.toEntity(domaineDTO);
        if (domaine.getLibelleDomaine()==null || domaine.getLibelleDomaine().trim().isEmpty()){
            throw new BadRequestAlertException("Le  LibelleDomaine est obligatoire", "LibelleDomaine", "LibelleDomaineNull");
        }
        if (domaineRepository.findByLibelleDomaine(domaine.getLibelleDomaine()).isPresent()) {
            throw new BadRequestAlertException("Une LibelleDomaine avec ce libellé existe déjà", "LibelleDomaine", "LibelleDomaineRegionExists");
        }
        domaine = domaineRepository.save(domaine);
        return domaineMapper.toDto(domaine);
    }

    @Override
    public Optional<DomaineDTO> partialUpdate(DomaineDTO domaineDTO) {
        log.debug("Request to partially update Domaine : {}", domaineDTO);
        if (domaineDTO.getLibelleDomaine()==null || domaineDTO.getLibelleDomaine().trim().isEmpty()){
            throw new BadRequestAlertException("Le  LibelleDomaine est obligatoire", "LibelleDomaine", "LibelleDomaineNull");
        }
        return domaineRepository
                   .findById(domaineDTO.getId())
                   .map(existingTypeFrais -> {
                       domaineRepository.findByLibelleDomaine(domaineDTO.getLibelleDomaine()).ifPresent(existing -> {
                           if (!existing.getId().equals(existingTypeFrais.getId())) {
                               throw new BadRequestAlertException("Une LibelleDomaine avec ce libellé existe déjà", "LibelleDomaine", "LibelleDomaineRegionExists");
                           }
                       });

                       domaineMapper.partialUpdate(existingTypeFrais, domaineDTO);
                       return existingTypeFrais;
                   })
                   .map(domaineRepository::save)
                   .map(domaineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DomaineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Domaines");
        return domaineRepository.findAll(pageable).map(domaineMapper::toDto);
    }

    public Page<DomaineDTO> findAllWithEagerRelationships(Pageable pageable) {
        return domaineRepository.findAllWithEagerRelationships(pageable).map(domaineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DomaineDTO> findOne(Long id) {
        log.debug("Request to get Domaine : {}", id);
        return domaineRepository.findOneWithEagerRelationships(id).map(domaineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Domaine : {}", id);
        domaineRepository.deleteById(id);
    }
//************************************************ FIND ALL DOMAINES BY UFR ********************************************
@Override
public Page<DomaineDTO> findAllDomaineByUfr(Long ufrId, Pageable pageable) {
    return domaineRepository.findByUfrsId(ufrId, pageable).map(domaineMapper::toDto);
}

//************************************************ FIND ALL DOMAINES BY UNIVERSITE *************************************
@Override
public Page<DomaineDTO> findAllDomaineByUniversite(Long universiteId, Pageable pageable) {
    return domaineRepository.findByUfrsUniversiteId(universiteId, pageable).map(domaineMapper::toDto);
}

//************************************************ FIND ALL DOMAINES BY MINISTERE **************************************
@Override
public Page<DomaineDTO> findAllDomaineByMinistere(Long ministereId, Pageable pageable) {
    return domaineRepository.findByUfrsUniversiteMinistereId(ministereId, pageable).map(domaineMapper::toDto);
}
}
