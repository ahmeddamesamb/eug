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
import sn.ugb.gir.repository.search.MinistereSearchRepository;
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

    private final MinistereSearchRepository ministereSearchRepository;

    public MinistereServiceImpl(
        MinistereRepository ministereRepository,
        MinistereMapper ministereMapper,
        MinistereSearchRepository ministereSearchRepository
    ) {
        this.ministereRepository = ministereRepository;
        this.ministereMapper = ministereMapper;
        this.ministereSearchRepository = ministereSearchRepository;
    }

    @Transactional
    @Override
    public MinistereDTO save(MinistereDTO ministereDTO) {
        log.debug("Request to save Ministere : {}", ministereDTO);

        validateMinistere(ministereDTO);
        Optional<Ministere> currentMinistere = ministereRepository.findByEnCoursYN(true);

        if (currentMinistere.isPresent()) {
            Ministere existingMinistere = currentMinistere.get();
            if (!LocalDate.now().isAfter(existingMinistere.getDateDebut())) {
                throw new BadRequestAlertException("La date de fin de debut de l'ancien Ministere doit être strictement antérieur à la date de début", ENTITY_NAME, "invalidOrdreDate");
            }
            existingMinistere.setEnCoursYN(false);
            existingMinistere.setDateFin(LocalDate.now());
            ministereRepository.save(existingMinistere);
        }

        if (ministereDTO.getEnCoursYN() == null) {
            ministereDTO.setEnCoursYN(true);
        }

        if (ministereDTO.getActifYN() == null) {
            ministereDTO.setActifYN(true);
        }

        if (ministereDTO.getDateFin() != null) {
            ministereDTO.setDateFin(null);
        }

        Ministere ministere = ministereMapper.toEntity(ministereDTO);
        ministere = ministereRepository.save(ministere);
        return ministereMapper.toDto(ministere);

    }



    @Transactional
    @Override
    public MinistereDTO update(MinistereDTO ministereDTO) {
        log.debug("Request to update Ministere : {}", ministereDTO);

        validateMinistere(ministereDTO);

        if (ministereDTO.getEnCoursYN() == true) {

            Optional<Ministere> currentMinistere = ministereRepository.findByEnCoursYN(true);

            if (currentMinistere.isPresent() && !currentMinistere.get().getId().equals(ministereDTO.getId())) {
                Ministere existingMinistere = currentMinistere.get();
                existingMinistere.setEnCoursYN(false);
                existingMinistere.setDateFin(LocalDate.now());
                ministereRepository.save(existingMinistere);
            }
            if (ministereDTO.getDateFin() != null) {
                ministereDTO.setDateFin(null);
            }
        }

        Ministere ministere = ministereMapper.toEntity(ministereDTO);
        if (ministereDTO.getEnCoursYN() == false && ministereRepository.findById(ministere.getId()).orElseThrow().getEnCoursYN() == true) {
            ministere.setDateFin(LocalDate.now());
        }

        ministere = ministereRepository.save(ministere);
        return ministereMapper.toDto(ministere);
    }

    @Override
    public Optional<MinistereDTO> partialUpdate(MinistereDTO ministereDTO) {

        log.debug("Request to partially update Ministere : {}", ministereDTO);

        validateMinistere(ministereDTO);

        return ministereRepository
            .findById(ministereDTO.getId())
            .map(existingMinistere -> {
                ministereMapper.partialUpdate(existingMinistere, ministereDTO);

                return existingMinistere;
            })
            .map(ministereRepository::save)
            .map(savedMinistere -> {
                ministereSearchRepository.index(savedMinistere);
                return savedMinistere;
            })
            .map(ministereMapper::toDto);
    }

    private void validateMinistere(MinistereDTO ministereDTO) {
        if (ministereDTO.getNomMinistere().isBlank()){
            throw new BadRequestAlertException("Le nom du ministere ne peut pas être vide.", ENTITY_NAME, "nomMinistereNotNull");
        }
        Optional<Ministere> ministereWithName = ministereRepository.findByNomMinistereIgnoreCase(ministereDTO.getNomMinistere());
        if (ministereWithName.isPresent() && !ministereWithName.get().getId().equals(ministereDTO.getId())) {
            throw new BadRequestAlertException("Un ministère avec ce nom existe déjà", ENTITY_NAME, "nomMinistereExists");
        }
        if (ministereDTO.getSigleMinistere().isBlank()) {
            throw new BadRequestAlertException("Le sigle du ministere ne peut pas être vide.", ENTITY_NAME, "sigleMinistereNotNull");
        }
        if (ministereDTO.getDateDebut() == null) {
            throw new BadRequestAlertException("La date de début ne peut pas être nulle.", ENTITY_NAME, "dateDebutNotNull");
        }
        if (ministereDTO.getDateFin() != null && !ministereDTO.getDateFin().isAfter(ministereDTO.getDateDebut())) {
            throw new BadRequestAlertException("La date de fin doit être strictement postérieure à la date de début", ENTITY_NAME, "invalidOrdreDate");
        }
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
        ministereSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MinistereDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ministeres for query {}", query);
        return ministereSearchRepository.search(query, pageable).map(ministereMapper::toDto);
    }

    @Override
    public Optional<MinistereDTO> findCurrent() {
        Ministere ministereEnCours = ministereRepository.findByEnCoursYN(true)
            .orElse(null);
        return Optional.ofNullable(ministereMapper.toDto(ministereEnCours));
    }

    @Override
    public Page<MinistereDTO> findByPeriode(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Request to get Ministeres by period: {} to {}", startDate, endDate);

        if (!endDate.isAfter(startDate)) {
            throw new BadRequestAlertException("La date de fin doit être strictement postérieure à la date de début", ENTITY_NAME, "invalidDateRange");
        }
        Page<Ministere> ministerePage = ministereRepository.findByDateDebutBetweenAndDateFinBetweenOrDateDebutBetweenAndEnCoursYN(startDate, endDate, startDate, endDate, startDate, endDate, true, pageable);
        return ministerePage.map(ministereMapper::toDto);
    }

    @Override
    public MinistereDTO activateOrDeactivate(Long id) {
        log.debug("Request to activate/deactivate Ministere : {}", id);
        Optional<Ministere> optionalMinistere = ministereRepository.findById(id);
        if (optionalMinistere.isPresent()) {
            Ministere ministere = optionalMinistere.get();
            ministere.setActifYN(!ministere.getActifYN());
            Ministere savedMinistere = ministereRepository.save(ministere);
            return ministereMapper.toDto(savedMinistere);
        } else {
            throw new BadRequestAlertException("Ce ministere n'est pas present " + id, ENTITY_NAME, "ministereIntrouvable");
        }
    }

}


