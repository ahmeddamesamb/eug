package sn.ugb.gateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.repository.InfoUserRessourceRepository;
import sn.ugb.gateway.repository.search.InfoUserRessourceSearchRepository;
import sn.ugb.gateway.service.InfoUserRessourceService;
import sn.ugb.gateway.service.dto.InfoUserRessourceDTO;
import sn.ugb.gateway.service.mapper.InfoUserRessourceMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gateway.domain.InfoUserRessource}.
 */
@Service
@Transactional
public class InfoUserRessourceServiceImpl implements InfoUserRessourceService {

    private final Logger log = LoggerFactory.getLogger(InfoUserRessourceServiceImpl.class);

    private final InfoUserRessourceRepository infoUserRessourceRepository;

    private final InfoUserRessourceMapper infoUserRessourceMapper;

    private final InfoUserRessourceSearchRepository infoUserRessourceSearchRepository;

    public InfoUserRessourceServiceImpl(
        InfoUserRessourceRepository infoUserRessourceRepository,
        InfoUserRessourceMapper infoUserRessourceMapper,
        InfoUserRessourceSearchRepository infoUserRessourceSearchRepository
    ) {
        this.infoUserRessourceRepository = infoUserRessourceRepository;
        this.infoUserRessourceMapper = infoUserRessourceMapper;
        this.infoUserRessourceSearchRepository = infoUserRessourceSearchRepository;
    }

    @Override
    public Mono<InfoUserRessourceDTO> save(InfoUserRessourceDTO infoUserRessourceDTO) {
        log.debug("Request to save InfoUserRessource : {}", infoUserRessourceDTO);
        return infoUserRessourceRepository
            .save(infoUserRessourceMapper.toEntity(infoUserRessourceDTO))
            .flatMap(infoUserRessourceSearchRepository::save)
            .map(infoUserRessourceMapper::toDto);
    }

    @Override
    public Mono<InfoUserRessourceDTO> update(InfoUserRessourceDTO infoUserRessourceDTO) {
        log.debug("Request to update InfoUserRessource : {}", infoUserRessourceDTO);
        return infoUserRessourceRepository
            .save(infoUserRessourceMapper.toEntity(infoUserRessourceDTO))
            .flatMap(infoUserRessourceSearchRepository::save)
            .map(infoUserRessourceMapper::toDto);
    }

    @Override
    public Mono<InfoUserRessourceDTO> partialUpdate(InfoUserRessourceDTO infoUserRessourceDTO) {
        log.debug("Request to partially update InfoUserRessource : {}", infoUserRessourceDTO);

        return infoUserRessourceRepository
            .findById(infoUserRessourceDTO.getId())
            .map(existingInfoUserRessource -> {
                infoUserRessourceMapper.partialUpdate(existingInfoUserRessource, infoUserRessourceDTO);

                return existingInfoUserRessource;
            })
            .flatMap(infoUserRessourceRepository::save)
            .flatMap(savedInfoUserRessource -> {
                infoUserRessourceSearchRepository.save(savedInfoUserRessource);
                return Mono.just(savedInfoUserRessource);
            })
            .map(infoUserRessourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<InfoUserRessourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InfoUserRessources");
        return infoUserRessourceRepository.findAllBy(pageable).map(infoUserRessourceMapper::toDto);
    }

    public Mono<Long> countAll() {
        return infoUserRessourceRepository.count();
    }

    public Mono<Long> searchCount() {
        return infoUserRessourceSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<InfoUserRessourceDTO> findOne(Long id) {
        log.debug("Request to get InfoUserRessource : {}", id);
        return infoUserRessourceRepository.findById(id).map(infoUserRessourceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete InfoUserRessource : {}", id);
        return infoUserRessourceRepository.deleteById(id).then(infoUserRessourceSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<InfoUserRessourceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InfoUserRessources for query {}", query);
        return infoUserRessourceSearchRepository.search(query, pageable).map(infoUserRessourceMapper::toDto);
    }

    @Override
    public Flux<InfoUserRessourceDTO> findAllByInfosUserId(Long infosUserId, Pageable pageable) {
        log.debug("Request to get all InfoUserRessources by InfosUserId : {}", infosUserId);
        return infoUserRessourceRepository.findAllByInfosUserId(infosUserId, pageable).map(infoUserRessourceMapper::toDto);
    }

    @Override
    public Flux<InfoUserRessourceDTO> findAllByInfosUserId(Long infosUserId) {
        return null;
    }


    @Override
    public Flux<InfoUserRessourceDTO> findAllByRessourceId(Long ressourceId) {
        log.debug("Request to get all InfoUserRessources by RessourceId : {}", ressourceId);
        return infoUserRessourceRepository.findAllByRessourceId(ressourceId).map(infoUserRessourceMapper::toDto);
    }

    @Override
    public Flux<InfoUserRessourceDTO> findAllByActifYN(Boolean actifYN) {
        log.debug("Request to get all InfoUserRessources by ActifYN : {}", actifYN);
        return infoUserRessourceRepository.findAllByActifYN(actifYN).map(infoUserRessourceMapper::toDto);
    }

    @Override
    public Mono<Void> archive(Long id, Boolean enCours) {
        return infoUserRessourceRepository.findById(id)
            .switchIfEmpty(Mono.error(new RuntimeException("InfoUserRessource not found with id: " + id)))
            .flatMap(infoUserRessource -> {
                infoUserRessource.setEnCoursYN(enCours);
                return infoUserRessourceRepository.save(infoUserRessource);
            })
            .then();
    }
}
