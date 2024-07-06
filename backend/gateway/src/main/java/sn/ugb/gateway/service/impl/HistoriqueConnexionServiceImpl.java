package sn.ugb.gateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.repository.HistoriqueConnexionRepository;
import sn.ugb.gateway.repository.search.HistoriqueConnexionSearchRepository;
import sn.ugb.gateway.service.HistoriqueConnexionService;
import sn.ugb.gateway.service.dto.HistoriqueConnexionDTO;
import sn.ugb.gateway.service.mapper.HistoriqueConnexionMapper;
import sn.ugb.gateway.web.rest.errors.BadRequestAlertException;

import java.time.LocalDate;

/**
 * Service Implementation for managing {@link sn.ugb.gateway.domain.HistoriqueConnexion}.
 */
@Service
@Transactional
public class HistoriqueConnexionServiceImpl implements HistoriqueConnexionService {

    private final Logger log = LoggerFactory.getLogger(HistoriqueConnexionServiceImpl.class);

    private final HistoriqueConnexionRepository historiqueConnexionRepository;

    private final HistoriqueConnexionMapper historiqueConnexionMapper;

    private final HistoriqueConnexionSearchRepository historiqueConnexionSearchRepository;

    public HistoriqueConnexionServiceImpl(
        HistoriqueConnexionRepository historiqueConnexionRepository,
        HistoriqueConnexionMapper historiqueConnexionMapper,
        HistoriqueConnexionSearchRepository historiqueConnexionSearchRepository
    ) {
        this.historiqueConnexionRepository = historiqueConnexionRepository;
        this.historiqueConnexionMapper = historiqueConnexionMapper;
        this.historiqueConnexionSearchRepository = historiqueConnexionSearchRepository;
    }

    @Override
    public Mono<HistoriqueConnexionDTO> save(HistoriqueConnexionDTO historiqueConnexionDTO) {
        log.debug("Request to save HistoriqueConnexion : {}", historiqueConnexionDTO);
        validateHistoriqueConnexion(historiqueConnexionDTO);
        return historiqueConnexionRepository
            .save(historiqueConnexionMapper.toEntity(historiqueConnexionDTO))
            .flatMap(historiqueConnexionSearchRepository::save)
            .map(historiqueConnexionMapper::toDto);
    }

    @Override
    public Mono<HistoriqueConnexionDTO> update(HistoriqueConnexionDTO historiqueConnexionDTO) {
        log.debug("Request to update HistoriqueConnexion : {}", historiqueConnexionDTO);
        validateHistoriqueConnexion(historiqueConnexionDTO);
        return historiqueConnexionRepository
            .save(historiqueConnexionMapper.toEntity(historiqueConnexionDTO))
            .flatMap(historiqueConnexionSearchRepository::save)
            .map(historiqueConnexionMapper::toDto);
    }

    @Override
    public Mono<HistoriqueConnexionDTO> partialUpdate(HistoriqueConnexionDTO historiqueConnexionDTO) {
        log.debug("Request to partially update HistoriqueConnexion : {}", historiqueConnexionDTO);
        validateHistoriqueConnexion(historiqueConnexionDTO);
        return historiqueConnexionRepository
            .findById(historiqueConnexionDTO.getId())
            .map(existingHistoriqueConnexion -> {
                historiqueConnexionMapper.partialUpdate(existingHistoriqueConnexion, historiqueConnexionDTO);

                return existingHistoriqueConnexion;
            })
            .flatMap(historiqueConnexionRepository::save)
            .flatMap(savedHistoriqueConnexion -> {
                historiqueConnexionSearchRepository.save(savedHistoriqueConnexion);
                return Mono.just(savedHistoriqueConnexion);
            })
            .map(historiqueConnexionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HistoriqueConnexionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HistoriqueConnexions");
        return historiqueConnexionRepository.findAllBy(pageable).map(historiqueConnexionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return historiqueConnexionRepository.count();
    }

    public Mono<Long> searchCount() {
        return historiqueConnexionSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<HistoriqueConnexionDTO> findOne(Long id) {
        log.debug("Request to get HistoriqueConnexion : {}", id);
        return historiqueConnexionRepository.findById(id).map(historiqueConnexionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete HistoriqueConnexion : {}", id);
        return historiqueConnexionRepository.deleteById(id).then(historiqueConnexionSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HistoriqueConnexionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HistoriqueConnexions for query {}", query);
        return historiqueConnexionSearchRepository.search(query, pageable).map(historiqueConnexionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HistoriqueConnexionDTO> getAllHistoriqueConnexionByEncoursYN(Boolean enCoursYN, Pageable pageable) {
        return historiqueConnexionRepository.findAllByEnCoursYN(enCoursYN, pageable).map(historiqueConnexionMapper::toDto);
    }

    @Override
    public Mono<HistoriqueConnexionDTO> archiveHistoriqueConnexion(Long id) {
        return historiqueConnexionRepository.findById(id)
            .flatMap(historiqueConnexion -> {
                historiqueConnexion.setActifYN(true);
                return historiqueConnexionRepository.save(historiqueConnexion);
            })
            .map(historiqueConnexionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HistoriqueConnexionDTO> getAllHistoriqueConnexionByTimeSlots(LocalDate dateDebut, LocalDate dateFin, Pageable pageable) {
        return historiqueConnexionRepository.findAllByTimeSlots(dateDebut, dateFin, pageable).map(historiqueConnexionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HistoriqueConnexionDTO> getAllHistoriqueConnexionByInfosUserIdAndTimeSlots(Long infosUserId, LocalDate dateDebut, LocalDate dateFin, Pageable pageable) {
        return historiqueConnexionRepository.findAllByInfosUserIdAndTimeSlots(infosUserId, dateDebut, dateFin, pageable).map(historiqueConnexionMapper::toDto);
    }

    @Override
    public Mono<Long> countAllByEnCoursYN(Boolean enCoursYN) {
        return historiqueConnexionRepository.countAllByEnCoursYN(enCoursYN);
    }

    @Override
    public Mono<Long> countAllByTimeSlots(LocalDate dateDebut, LocalDate dateFin) {
        return historiqueConnexionRepository.countAllByTimeSlots(dateDebut, dateFin);
    }

    @Override
    public Mono<Long> countAllByInfosUserIdAndTimeSlots(Long infosUserId, LocalDate dateDebut, LocalDate dateFin) {
        return historiqueConnexionRepository.countAllByInfosUserIdAndTimeSlots(infosUserId, dateDebut, dateFin);
    }

    public void validateHistoriqueConnexion(HistoriqueConnexionDTO historiqueConnexionDTO) {
        // Tous les champs obligatoires sont présents, sauf date fin et durée de session
        if (historiqueConnexionDTO.getDateDebutConnexion() == null) {
            throw new BadRequestAlertException("La date de debut de connexion ne peut pas etre vide", "HistoriqueConnexion", "DateDebutConnexionNotNull");
        }

        if (historiqueConnexionDTO.getAdresseIp() == null) {
            throw new BadRequestAlertException("L'adrresse ip ne peut pas etre vide", "HistoriqueConnexion", "AdresseIpNotNull");
        }

        if (historiqueConnexionDTO.getInfoUser() == null) {
            throw new BadRequestAlertException("InfoUser ne peut pas etre vide", "HistoriqueConnexion", "InfoUserNotNull");
        }
    }
}
