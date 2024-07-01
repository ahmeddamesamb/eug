package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Ufr;
import sn.ugb.gir.repository.UfrRepository;
import sn.ugb.gir.repository.search.UfrSearchRepository;
import sn.ugb.gir.service.UfrService;
import sn.ugb.gir.service.dto.UfrDTO;
import sn.ugb.gir.service.mapper.UfrMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Ufr}.
 */
@Service
@Transactional
public class UfrServiceImpl implements UfrService {

    private final Logger log = LoggerFactory.getLogger(UfrServiceImpl.class);

    private final UfrRepository ufrRepository;

    private final UfrMapper ufrMapper;

    private final UfrSearchRepository ufrSearchRepository;

    private static final String ENTITY_NAME = "microserviceGirUfr";

    public UfrServiceImpl(UfrRepository ufrRepository, UfrMapper ufrMapper, UfrSearchRepository ufrSearchRepository) {
        this.ufrRepository = ufrRepository;
        this.ufrMapper = ufrMapper;
        this.ufrSearchRepository = ufrSearchRepository;
    }

    @Override
    public UfrDTO save(UfrDTO ufrDTO) {
        log.debug("Request to save Ufr : {}", ufrDTO);
        validateData(ufrDTO);
        Ufr ufr = ufrMapper.toEntity(ufrDTO);
        ufr = ufrRepository.save(ufr);
        UfrDTO result = ufrMapper.toDto(ufr);
        ufrSearchRepository.index(ufr);
        return result;
    }

    @Override
    public UfrDTO update(UfrDTO ufrDTO) {
        log.debug("Request to update Ufr : {}", ufrDTO);
        validateData(ufrDTO);
        Ufr ufr = ufrMapper.toEntity(ufrDTO);
        ufr = ufrRepository.save(ufr);
        UfrDTO result = ufrMapper.toDto(ufr);
        ufrSearchRepository.index(ufr);
        return result;
    }

    @Override
    public Optional<UfrDTO> partialUpdate(UfrDTO ufrDTO) {
        log.debug("Request to partially update Ufr : {}", ufrDTO);
        validateData(ufrDTO);
        return ufrRepository
            .findById(ufrDTO.getId())
            .map(existingUfr -> {
                ufrMapper.partialUpdate(existingUfr, ufrDTO);

                return existingUfr;
            })
            .map(ufrRepository::save)
            .map(savedUfr -> {
                ufrSearchRepository.index(savedUfr);
                return savedUfr;
            })
            .map(ufrMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UfrDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ufrs");
        return ufrRepository.findAll(pageable).map(ufrMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UfrDTO> findOne(Long id) {
        log.debug("Request to get Ufr : {}", id);
        return ufrRepository.findById(id).map(ufrMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ufr : {}", id);
        ufrRepository.deleteById(id);
        ufrSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UfrDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ufrs for query {}", query);
        return ufrSearchRepository.search(query, pageable).map(ufrMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UfrDTO> getAllUfrByUniversite(Long universiteId, Pageable pageable) {
        log.debug("Request to get all UFRs by Universite ID : {}", universiteId);
        return ufrRepository.findByUniversiteId(universiteId, pageable)
            .map(ufrMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UfrDTO> getAllUfrByMinistere(Long ministereId, Pageable pageable) {
        log.debug("Request to get all UFRs by Ministere ID : {}", ministereId);
        return ufrRepository.findByUniversiteMinistereId(ministereId, pageable)
            .map(ufrMapper::toDto);
    }

    @Override
    public UfrDTO setActifYNUfr(Long id, Boolean actifYN) {
        Ufr ufr = ufrRepository.findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Ufr not found.", ENTITY_NAME, "UfrNotFound"));
        ufr.setActifYN(actifYN);
        ufr = ufrRepository.save(ufr);
        return ufrMapper.toDto(ufr);
    }

    private void validateData(UfrDTO ufrDTO) {
        if (ufrDTO.getLibelleUfr().isBlank()){
            throw new BadRequestAlertException("Le libellé ne peut pas être vide.", ENTITY_NAME, "libelleUfrNotNull");
        }
        if (ufrDTO.getSigleUfr().isBlank()){
            throw new BadRequestAlertException("La sigle ne peut pas être vide.", ENTITY_NAME, "sigleUfrNotNull");
        }
        Optional<Ufr> existingUfr = ufrRepository.findByLibelleUfrIgnoreCase(ufrDTO.getLibelleUfr());
        if (existingUfr.isPresent() && !existingUfr.get().getId().equals(ufrDTO.getId())) {
            throw new BadRequestAlertException("Une ufr avec le même libellé existe.", ENTITY_NAME, "libelleUfrExist");
        }
        existingUfr = ufrRepository.findBySigleUfrIgnoreCase(ufrDTO.getSigleUfr());
        if (existingUfr.isPresent() && !existingUfr.get().getId().equals(ufrDTO.getId())) {
            throw new BadRequestAlertException("Une ufr avec la même sigle existe.", ENTITY_NAME, "sigleUfrExist");
        }
    }
}
