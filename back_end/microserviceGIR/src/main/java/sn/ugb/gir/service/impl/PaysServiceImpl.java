package sn.ugb.gir.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Domaine;
import sn.ugb.gir.domain.Pays;
import sn.ugb.gir.repository.PaysRepository;
import sn.ugb.gir.service.PaysService;
import sn.ugb.gir.service.dto.DomaineDTO;
import sn.ugb.gir.service.dto.PaysDTO;
import sn.ugb.gir.service.mapper.PaysMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import java.util.Optional;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Pays}.
 */
@Service
@Transactional
public class PaysServiceImpl implements PaysService {

private final Logger log = LoggerFactory.getLogger(PaysServiceImpl.class);

private final PaysRepository paysRepository;

private final PaysMapper paysMapper;

public PaysServiceImpl(PaysRepository paysRepository, PaysMapper paysMapper) {
    this.paysRepository = paysRepository;
    this.paysMapper = paysMapper;
}

@Override
public PaysDTO save(PaysDTO paysDTO) {
    log.debug("Request to save Pays : {}", paysDTO);
    validateData(paysDTO);
    Pays pays = paysMapper.toEntity(paysDTO);
    pays = paysRepository.save(pays);
    return paysMapper.toDto(pays);
}

@Override
public PaysDTO update(PaysDTO paysDTO) {
    log.debug("Request to update Pays : {}", paysDTO);
    validateData(paysDTO);
    Pays pays = paysMapper.toEntity(paysDTO);
    pays = paysRepository.save(pays);
    return paysMapper.toDto(pays);
}

@Override
public Optional<PaysDTO> partialUpdate(PaysDTO paysDTO) {
    log.debug("Request to partially update Pays : {}", paysDTO);
    validateData(paysDTO);
    return paysRepository
               .findById(paysDTO.getId())
               .map(existingPays -> {
                   paysRepository.findByLibellePaysIgnoreCase(paysDTO.getLibellePays()).ifPresent(existing -> {
                       if (!existing.getId().equals(existingPays.getId())) {
                           throw new BadRequestAlertException("Une Region avec ce libellé existe déjà", "pays", "libellePaysExists");
                       }
                   });

                   paysMapper.partialUpdate(existingPays, paysDTO);
                   return existingPays;
               })
               .map(paysRepository::save)
               .map(paysMapper::toDto);
}

@Override
@Transactional(readOnly = true)
public Page<PaysDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Pays");
    return paysRepository.findAll(pageable).map(paysMapper::toDto);
}

public Page<PaysDTO> findAllWithEagerRelationships(Pageable pageable) {
    return paysRepository.findAllWithEagerRelationships(pageable).map(paysMapper::toDto);
}

@Override
@Transactional(readOnly = true)
public Optional<PaysDTO> findOne(Long id) {
    log.debug("Request to get Pays : {}", id);
    return paysRepository.findOneWithEagerRelationships(id).map(paysMapper::toDto);
}

@Override
public void delete(Long id) {
    log.debug("Request to delete Pays : {}", id);
    paysRepository.deleteById(id);
}
//************************************************ FIND ALL PAYS BY ZONE ID ********************************************
@Override
public Page<PaysDTO> findAllPaysByZone(Long zoneId, Pageable pageable) {
    return paysRepository.findByZonesId(zoneId, pageable).map(paysMapper::toDto);
}
//************************************************ FONCTION TESTE VALIDATE DATA **************************************

private void validateData(PaysDTO paysDTO) {
    if (paysDTO.getLibellePays().isEmpty() || paysDTO.getLibellePays().isBlank()){
        throw new BadRequestAlertException("Le Pays ne peut pas être vide.", ENTITY_NAME, "getLibellePaysNotNull");
    }
    if (paysDTO.getNationalite().isEmpty() || paysDTO.getNationalite().isBlank()){
        throw new BadRequestAlertException("Le Pays ne peut pas être vide.", ENTITY_NAME, "getLibellePaysNotNull");
    }
    if (paysDTO.getCodePays().isEmpty() || paysDTO.getCodePays().isBlank()){
        throw new BadRequestAlertException("Le Pays ne peut pas être vide.", ENTITY_NAME, "getLibellePaysNotNull");
    }
    Optional<Pays> existingPays = paysRepository.findByLibellePaysIgnoreCase(paysDTO.getLibellePays());
    if (existingPays.isPresent() && !existingPays.get().getId().equals(paysDTO.getId())) {
        throw new BadRequestAlertException("Un Pays avec le même libellé existe.", ENTITY_NAME, "getLibellePaysExist");
    }
}

}
