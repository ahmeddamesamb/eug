package sn.ugb.gateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.repository.RessourceRepository;
import sn.ugb.gateway.repository.search.RessourceSearchRepository;
import sn.ugb.gateway.service.RessourceService;
import sn.ugb.gateway.service.dto.RessourceDTO;
import sn.ugb.gateway.service.mapper.RessourceMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gateway.domain.Ressource}.
 */
@Service
@Transactional
public class RessourceServiceImpl implements RessourceService {

    private final Logger log = LoggerFactory.getLogger(RessourceServiceImpl.class);

    private final RessourceRepository ressourceRepository;

    private final RessourceMapper ressourceMapper;

    private final RessourceSearchRepository ressourceSearchRepository;

    public RessourceServiceImpl(
        RessourceRepository ressourceRepository,
        RessourceMapper ressourceMapper,
        RessourceSearchRepository ressourceSearchRepository
    ) {
        this.ressourceRepository = ressourceRepository;
        this.ressourceMapper = ressourceMapper;
        this.ressourceSearchRepository = ressourceSearchRepository;
    }

    @Override
    public Mono<RessourceDTO> save(RessourceDTO ressourceDTO) {
        log.debug("Request to save Ressource : {}", ressourceDTO);
        return ressourceRepository
            .save(ressourceMapper.toEntity(ressourceDTO))
            .flatMap(ressourceSearchRepository::save)
            .map(ressourceMapper::toDto);
    }

    @Override
    public Mono<RessourceDTO> update(RessourceDTO ressourceDTO) {
        log.debug("Request to update Ressource : {}", ressourceDTO);
        return ressourceRepository
            .save(ressourceMapper.toEntity(ressourceDTO))
            .flatMap(ressourceSearchRepository::save)
            .map(ressourceMapper::toDto);
    }

    @Override
    public Mono<RessourceDTO> partialUpdate(RessourceDTO ressourceDTO) {
        log.debug("Request to partially update Ressource : {}", ressourceDTO);

        return ressourceRepository
            .findById(ressourceDTO.getId())
            .map(existingRessource -> {
                ressourceMapper.partialUpdate(existingRessource, ressourceDTO);

                return existingRessource;
            })
            .flatMap(ressourceRepository::save)
            .flatMap(savedRessource -> {
                ressourceSearchRepository.save(savedRessource);
                return Mono.just(savedRessource);
            })
            .map(ressourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<RessourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ressources");
        return ressourceRepository.findAllBy(pageable).map(ressourceMapper::toDto);
    }

    public Mono<Long> countAll() {
        return ressourceRepository.count();
    }

    public Mono<Long> searchCount() {
        return ressourceSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<RessourceDTO> findOne(Long id) {
        log.debug("Request to get Ressource : {}", id);
        return ressourceRepository.findById(id).map(ressourceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Ressource : {}", id);
        return ressourceRepository.deleteById(id).then(ressourceSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<RessourceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ressources for query {}", query);
        return ressourceSearchRepository.search(query, pageable).map(ressourceMapper::toDto);
    }
@Override
public Page<RessourceDTO> findAllRessourceByService(Long serviceId, Pageable pageable) {
    return ressourceRepository.findByServiceId(serviceId, pageable):map(ressourceMapper::toDto);
}

@Override
public Page<RessourceDTO> findAllRessourceByBlocfonctionnel(Long blocfonctionnelId, Pageable pageable) {
    return ressourceRepository.findByBlocfonctionnelId(blocfonctionnelId, pageable).map(ressourceMapper::toDto);
}

@Override
public RessourceDTO setActifYNRessource(Long id, Boolean actifYN) {
    Ressource ressource = ressourceRepository.findById(id)
                              .orElseThrow(() -> new BadRequestAlertException("Ressource not found.", ENTITY_NAME, "ressourceNotFound"));
    ressource.setActifYN(actifYN);
    ressource = ressourceRepository.save(ressource);
    return ressourceMapper.toDto(ressource);

    }

private void validateData(RessourceDTO ressourceDTO) {
    if (ressourceDTO.getLibelle().isEmpty() || ressourceDTO.getLibelle().isBlank()){
        throw new BadRequestAlertException("Le Ressource ne peut pas être vide.", ENTITY_NAME, "getLibelleRessourceNotNull");
    }
//    if (ressourceDTO.getNationalite().isEmpty() || ressourceDTO.getNationalite().isBlank()){
//        throw new BadRequestAlertException("Le Ressource ne peut pas être vide.", ENTITY_NAME, "getLibelleRessourceNotNull");
//    }
//    if (ressourceDTO.getCodeRessource().isEmpty() || ressourceDTO.getCodeRessource().isBlank()){
//        throw new BadRequestAlertException("Le Ressource ne peut pas être vide.", ENTITY_NAME, "getLibelleRessourceNotNull");
//    }
//    Optional<Ressource> existingRessource = ressourceRepository.findByLibelleRessourceIgnoreCase(ressourceDTO.getLibelle());
//    if (existingRessource.isPresent() && !existingRessource.get().getId().equals(ressourceDTO.getId())) {
//        throw new BadRequestAlertException("Un Ressource avec le même libellé existe.", ENTITY_NAME, "getLibelleRessourceExist");
//    }
//    @Override
//    public Boolean setRessourceActifYN(Boolean actifYN) {
//        this.setRessourceActifYN(actifYN);
//        return true;
//    }
}
}
