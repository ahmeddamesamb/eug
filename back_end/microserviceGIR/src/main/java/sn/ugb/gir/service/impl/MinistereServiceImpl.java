package sn.ugb.gir.service.impl;

import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Ministere;
import sn.ugb.gir.repository.MinistereRepository;
import sn.ugb.gir.service.MinistereService;
import sn.ugb.gir.service.dto.MinistereDTO;
import sn.ugb.gir.service.mapper.MinistereMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Ministere}.
 */
@Service
@Transactional
public class MinistereServiceImpl implements MinistereService {

    private final Logger log = LoggerFactory.getLogger(MinistereServiceImpl.class);

    private static final String ENTITY_NAME = "Ministere";

    private final MinistereRepository ministereRepository;

    private final MinistereMapper ministereMapper;

    public MinistereServiceImpl(MinistereRepository ministereRepository, MinistereMapper ministereMapper) {
        this.ministereRepository = ministereRepository;
        this.ministereMapper = ministereMapper;
    }

    @Transactional
    @Override
    public MinistereDTO save(MinistereDTO ministereDTO) {
        log.debug("Request to save Ministere : {}", ministereDTO);

        if (ministereRepository.findByNomMinistereIgnoreCase(ministereDTO.getNomMinistere()).isPresent()) {
            throw new BadRequestAlertException("Un ministère avec ce nom existe déjà", ENTITY_NAME, "nomMinistereExists");
        }

        Optional<Ministere> currentMinistere = ministereRepository.findByEnCoursYN(1);

        if (currentMinistere.isPresent()) {
            Ministere existingMinistere = currentMinistere.get();
            existingMinistere.setEnCoursYN(0);
            existingMinistere.setDateFin(LocalDate.now());
            ministereRepository.save(existingMinistere);
        }

        if (ministereDTO.getEnCoursYN() == null) {
            ministereDTO.setEnCoursYN(1);
        }

        if (ministereDTO.getDateFin() != null && !ministereDTO.getDateFin().isAfter(ministereDTO.getDateDebut())) {
            throw new BadRequestAlertException("La date de fin doit être strictement postérieure à la date de début", ENTITY_NAME, "invalidDateRange");
        }

        Ministere ministere = ministereMapper.toEntity(ministereDTO);
        ministere = ministereRepository.save(ministere);
        return ministereMapper.toDto(ministere);
    }

    @Transactional
    @Override
    public MinistereDTO update(MinistereDTO ministereDTO) {
        log.debug("Request to update Ministere : {}", ministereDTO);

        Optional<Ministere> ministereWithName = ministereRepository.findByNomMinistereIgnoreCase(ministereDTO.getNomMinistere());

        if (ministereWithName.isPresent() && !ministereWithName.get().getId().equals(ministereDTO.getId())) {
            throw new BadRequestAlertException("Un ministère avec ce nom existe déjà", ENTITY_NAME, "nomMinistereExists");
        }

        if (ministereDTO.getEnCoursYN() == 1) {
            Optional<Ministere> currentMinistere = ministereRepository.findByEnCoursYN(1);

            if (currentMinistere.isPresent() && !currentMinistere.get().getId().equals(ministereDTO.getId())) {
                Ministere existingMinistere = currentMinistere.get();
                existingMinistere.setEnCoursYN(0);
                existingMinistere.setDateFin(LocalDate.now());
                ministereRepository.save(existingMinistere);
            }
        }

        if (ministereDTO.getDateFin() != null && !ministereDTO.getDateFin().isAfter(ministereDTO.getDateDebut())) {
            throw new BadRequestAlertException("La date de fin doit être strictement postérieure à la date de début", ENTITY_NAME, "invalidDateRange");
        }

        Ministere ministere = ministereMapper.toEntity(ministereDTO);

        if (ministereDTO.getEnCoursYN() == 0 && ministereRepository.findById(ministere.getId()).orElseThrow().getEnCoursYN() == 1) {
            ministere.setDateFin(LocalDate.now());
        }

        ministere = ministereRepository.save(ministere);

        return ministereMapper.toDto(ministere);
    }



    @Override
    public Optional<MinistereDTO> partialUpdate(MinistereDTO ministereDTO) {
        log.debug("Request to partially update Ministere : {}", ministereDTO);

        return ministereRepository
            .findById(ministereDTO.getId())
            .map(existingMinistere -> {
                ministereMapper.partialUpdate(existingMinistere, ministereDTO);

                return existingMinistere;
            })
            .map(ministereRepository::save)
            .map(ministereMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MinistereDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ministeres");
        return ministereRepository.findAll(pageable).map(ministereMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MinistereDTO> findOne(Long id) {
        log.debug("Request to get Ministere : {}", id);
        return ministereRepository.findById(id).map(ministereMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ministere : {}", id);
        ministereRepository.deleteById(id);
    }


    /**
     * @return
     */
    @Override
    public Optional<MinistereDTO> findCurrent() {
        Ministere ministereEnCours = ministereRepository.findByEnCoursYN(1)
            .orElse(null);
        return Optional.ofNullable(ministereMapper.toDto(ministereEnCours));
    }

    @Override
    public Page<MinistereDTO> findByPeriode(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Request to get Ministeres by period: {} to {}", startDate, endDate);
        Page<Ministere> ministerePage = ministereRepository.findByDateDebutBetweenAndDateFinBetween(startDate, endDate, startDate, endDate, pageable);
        return ministerePage.map(ministereMapper::toDto);
    }
}
