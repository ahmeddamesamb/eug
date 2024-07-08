package sn.ugb.gir.service.impl;

import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Campagne;
import sn.ugb.gir.repository.CampagneRepository;
import sn.ugb.gir.repository.search.CampagneSearchRepository;
import sn.ugb.gir.service.CampagneService;
import sn.ugb.gir.service.dto.CampagneDTO;
import sn.ugb.gir.service.mapper.CampagneMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Campagne}.
 */
@Service
@Transactional
public class CampagneServiceImpl implements CampagneService {

    private final Logger log = LoggerFactory.getLogger(CampagneServiceImpl.class);

    private final CampagneRepository campagneRepository;

    private final CampagneMapper campagneMapper;

    private final CampagneSearchRepository campagneSearchRepository;

    private static final String ENTITY_NAME = "microserviceGirCampagne";

    public CampagneServiceImpl(
        CampagneRepository campagneRepository,
        CampagneMapper campagneMapper,
        CampagneSearchRepository campagneSearchRepository
    ) {
        this.campagneRepository = campagneRepository;
        this.campagneMapper = campagneMapper;
        this.campagneSearchRepository = campagneSearchRepository;
    }

    @Override
    public CampagneDTO save(CampagneDTO campagneDTO) {
        log.debug("Request to save Campagne : {}", campagneDTO);
        Campagne campagne = campagneMapper.toEntity(campagneDTO);
        validateCampagne(campagne);
        campagne = campagneRepository.save(campagne);
        CampagneDTO result = campagneMapper.toDto(campagne);
        campagneSearchRepository.index(campagne);
        return result;
    }

    @Override
    public CampagneDTO update(CampagneDTO campagneDTO) {
        log.debug("Request to update Campagne : {}", campagneDTO);
        Campagne campagne = campagneMapper.toEntity(campagneDTO);
        validateCampagne(campagne);
        campagne = campagneRepository.save(campagne);
        CampagneDTO result = campagneMapper.toDto(campagne);
        campagneSearchRepository.index(campagne);
        return result;
    }

    @Override
    public Optional<CampagneDTO> partialUpdate(CampagneDTO campagneDTO) {
        log.debug("Request to partially update Campagne : {}", campagneDTO);
        Campagne campagne = campagneMapper.toEntity(campagneDTO);
        validateCampagne(campagne);
        return campagneRepository
            .findById(campagneDTO.getId())
            .map(existingCampagne -> {
                campagneMapper.partialUpdate(existingCampagne, campagneDTO);

                return existingCampagne;
            })
            .map(campagneRepository::save)
            .map(savedCampagne -> {
                campagneSearchRepository.index(savedCampagne);
                return savedCampagne;
            })
            .map(campagneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CampagneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Campagnes");
        return campagneRepository.findAll(pageable).map(campagneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CampagneDTO> findOne(Long id) {
        log.debug("Request to get Campagne : {}", id);
        return campagneRepository.findById(id).map(campagneMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Campagne : {}", id);
        campagneRepository.deleteById(id);
        campagneSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CampagneDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Campagnes for query {}", query);
        return campagneSearchRepository.search(query, pageable).map(campagneMapper::toDto);
    }

    private void validateCampagne(Campagne campagne) {
        if (campagne.getDateDebut() == null){
            throw new BadRequestAlertException("La dateDebut ne peut pas être vide.", ENTITY_NAME, "DateDebutCampagneNotNull");
        }

        if (campagne.getDateFin() == null){
            throw new BadRequestAlertException("La dateFin ne peut pas être vide.", ENTITY_NAME, "DateFinCampagneNotNull");
        }

        // RG1: La date de début ne doit pas être postérieure à la date de fin
        if (campagne.getDateDebut().isAfter(campagne.getDateFin())) {
            throw new BadRequestAlertException("La date de début ne doit pas être postérieure à la date de fin.",ENTITY_NAME,"dateDebutNotPosteriorDateFin");
        }

        // RG3: On ne peut pas programmer une campagne dans le passé
        if (campagne.getDateDebut().isBefore(LocalDate.now())) {
            throw new BadRequestAlertException("On ne peut pas programmer une formation/campagne dans le passé.",ENTITY_NAME,"noFormationCampaignInPast");
        }

        Optional<Campagne> existingCampagne = campagneRepository.findByDateDebutAndDateFin(campagne.getDateDebut(),campagne.getDateFin());
        if (existingCampagne.isPresent() && !existingCampagne.get().getId().equals(campagne.getId())){
            throw new BadRequestAlertException("Une campagne avec les memes dates (dateDebut et dateFin) existe.", ENTITY_NAME, "campagneExist");
        }
    }
}
