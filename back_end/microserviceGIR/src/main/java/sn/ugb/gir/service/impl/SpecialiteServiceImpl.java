package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Specialite;
import sn.ugb.gir.domain.Ufr;
import sn.ugb.gir.repository.SpecialiteRepository;
import sn.ugb.gir.service.SpecialiteService;
import sn.ugb.gir.service.dto.SpecialiteDTO;
import sn.ugb.gir.service.dto.UfrDTO;
import sn.ugb.gir.service.mapper.SpecialiteMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Specialite}.
 */
@Service
@Transactional
public class SpecialiteServiceImpl implements SpecialiteService {

    private final Logger log = LoggerFactory.getLogger(SpecialiteServiceImpl.class);

    private final SpecialiteRepository specialiteRepository;

    private final SpecialiteMapper specialiteMapper;

    private static final String ENTITY_NAME = "microserviceGirSpecialite";

    public SpecialiteServiceImpl(SpecialiteRepository specialiteRepository, SpecialiteMapper specialiteMapper) {
        this.specialiteRepository = specialiteRepository;
        this.specialiteMapper = specialiteMapper;
    }

    @Override
    public SpecialiteDTO save(SpecialiteDTO specialiteDTO) {
        log.debug("Request to save Specialite : {}", specialiteDTO);
        validateData(specialiteDTO);
        Specialite specialite = specialiteMapper.toEntity(specialiteDTO);
        specialite = specialiteRepository.save(specialite);
        return specialiteMapper.toDto(specialite);
    }

    @Override
    public SpecialiteDTO update(SpecialiteDTO specialiteDTO) {
        log.debug("Request to update Specialite : {}", specialiteDTO);
        validateData(specialiteDTO);
        Specialite specialite = specialiteMapper.toEntity(specialiteDTO);
        specialite = specialiteRepository.save(specialite);
        return specialiteMapper.toDto(specialite);
    }

    @Override
    public Optional<SpecialiteDTO> partialUpdate(SpecialiteDTO specialiteDTO) {
        log.debug("Request to partially update Specialite : {}", specialiteDTO);
        validateData(specialiteDTO);
        return specialiteRepository
            .findById(specialiteDTO.getId())
            .map(existingSpecialite -> {
                specialiteMapper.partialUpdate(existingSpecialite, specialiteDTO);

                return existingSpecialite;
            })
            .map(specialiteRepository::save)
            .map(specialiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialiteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Specialites");
        return specialiteRepository.findAll(pageable).map(specialiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpecialiteDTO> findOne(Long id) {
        log.debug("Request to get Specialite : {}", id);
        return specialiteRepository.findById(id).map(specialiteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Specialite : {}", id);
        specialiteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialiteDTO> getAllSpecialiteByMention(Long mentionId, Pageable pageable) {
        log.debug("Request to get all Specialites by Mention ID : {}", mentionId);
        return specialiteRepository.findByMentionId(mentionId, pageable).map(specialiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialiteDTO> getAllSpecialiteByDomaine(Long domaineId, Pageable pageable) {
        log.debug("Request to get all Specialites by Domaine ID : {}", domaineId);
        return specialiteRepository.findByMentionDomaineId(domaineId, pageable).map(specialiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialiteDTO> getAllSpecialiteByUfr(Long ufrId, Pageable pageable) {
        log.debug("Request to get all Specialites by UFR ID : {}", ufrId);
        return specialiteRepository.findByMentionDomaineUfrsId(ufrId, pageable).map(specialiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialiteDTO> getAllSpecialiteByUniversite(Long universiteId, Pageable pageable) {
        log.debug("Request to get all Specialites by Universite ID : {}", universiteId);
        return specialiteRepository.findByMentionDomaineUfrsUniversiteId(universiteId, pageable).map(specialiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialiteDTO> getAllSpecialiteByMinistere(Long ministereId, Pageable pageable) {
        log.debug("Request to get all Specialites by Ministere ID : {}", ministereId);
        return specialiteRepository.findByMentionDomaineUfrsUniversiteMinistereId(ministereId, pageable).map(specialiteMapper::toDto);
    }

    private void validateData(SpecialiteDTO specialiteDTO) {
        if (specialiteDTO.getNomSpecialites().isBlank()){
            throw new BadRequestAlertException("Le libellé ne peut pas être vide.", ENTITY_NAME, "nomSpecialiteNotNull");
        }
        if (specialiteDTO.getSigleSpecialites().isBlank()){
            throw new BadRequestAlertException("La sigle ne peut pas être vide.", ENTITY_NAME, "sigleSpecialiteNotNull");
        }
        Optional<Specialite> existingSpecialite = specialiteRepository.findByNomSpecialitesIgnoreCase(specialiteDTO.getNomSpecialites());
        if (existingSpecialite.isPresent() && !existingSpecialite.get().getId().equals(specialiteDTO.getId())) {
            throw new BadRequestAlertException("Une ufr avec le même libellé existe.", ENTITY_NAME, "nomSpecialiteExist");
        }
        existingSpecialite = specialiteRepository.findBySigleSpecialitesIgnoreCase(specialiteDTO.getSigleSpecialites());
        if (existingSpecialite.isPresent() && !existingSpecialite.get().getId().equals(specialiteDTO.getId())) {
            throw new BadRequestAlertException("Une ufr avec la même sigle existe.", ENTITY_NAME, "sigleSpecialiteExist");
        }
    }
}
