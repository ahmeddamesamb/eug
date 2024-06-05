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

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

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
        validateData(domaineDTO);
        Domaine domaine = domaineMapper.toEntity(domaineDTO);
        domaine = domaineRepository.save(domaine);
        return domaineMapper.toDto(domaine);
    }

    @Override
    public DomaineDTO update(DomaineDTO domaineDTO) {
        log.debug("Request to update Domaine : {}", domaineDTO);
        validateData(domaineDTO);
        Domaine domaine = domaineMapper.toEntity(domaineDTO);
        domaine = domaineRepository.save(domaine);
        return domaineMapper.toDto(domaine);
    }

    @Override
    public Optional<DomaineDTO> partialUpdate(DomaineDTO domaineDTO) {
        log.debug("Request to partially update Domaine : {}", domaineDTO);
        validateData(domaineDTO);
        return domaineRepository
                   .findById(domaineDTO.getId())
                   .map(existingDomaine -> {
                       domaineRepository.findByLibelleDomaineIgnoreCase(domaineDTO.getLibelleDomaine()).ifPresent(existing -> {
                           if (!existing.getId().equals(existingDomaine.getId())) {
                               throw new BadRequestAlertException("Une LibelleDomaine avec ce libellé existe déjà", "LibelleDomaine", "LibelleDomaineRegionExists");
                           }
                       });

                       domaineMapper.partialUpdate(existingDomaine, domaineDTO);
                       return existingDomaine;
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
@Override
public Page<DomaineDTO> findAllDomaineByUfr(Long ufrId, Pageable pageable) {
    return domaineRepository.findByUfrsId(ufrId, pageable).map(domaineMapper::toDto);
}

@Override
public Page<DomaineDTO> findAllDomaineByUniversite(Long universiteId, Pageable pageable) {
    return domaineRepository.findByUfrsUniversiteId(universiteId, pageable).map(domaineMapper::toDto);
}

@Override
public Page<DomaineDTO> findAllDomaineByMinistere(Long ministereId, Pageable pageable) {
    return domaineRepository.findByUfrsUniversiteMinistereId(ministereId, pageable).map(domaineMapper::toDto);
}

private void validateData(DomaineDTO domaineDto) {
    if (domaineDto.getLibelleDomaine().isEmpty() || domaineDto.getLibelleDomaine().isBlank()){
        throw new BadRequestAlertException("Le Domaine ne peut pas être vide.", ENTITY_NAME, "getLibelleDomaineNotNull");
    }
    Optional<Domaine> existingDomaine = domaineRepository.findByLibelleDomaineIgnoreCase(domaineDto.getLibelleDomaine());
    if (existingDomaine.isPresent() && !existingDomaine.get().getId().equals(domaineDto.getId())) {
        throw new BadRequestAlertException("Un Domaine avec le même libellé existe.", ENTITY_NAME, "getLibelleDomaineExist");
    }
}
}
