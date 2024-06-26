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
        return historiqueConnexionRepository
            .save(historiqueConnexionMapper.toEntity(historiqueConnexionDTO))
            .flatMap(historiqueConnexionSearchRepository::save)
            .map(historiqueConnexionMapper::toDto);
    }

    @Override
    public Mono<HistoriqueConnexionDTO> update(HistoriqueConnexionDTO historiqueConnexionDTO) {
        log.debug("Request to update HistoriqueConnexion : {}", historiqueConnexionDTO);
        return historiqueConnexionRepository
            .save(historiqueConnexionMapper.toEntity(historiqueConnexionDTO))
            .flatMap(historiqueConnexionSearchRepository::save)
            .map(historiqueConnexionMapper::toDto);
    }

    @Override
    public Mono<HistoriqueConnexionDTO> partialUpdate(HistoriqueConnexionDTO historiqueConnexionDTO) {
        log.debug("Request to partially update HistoriqueConnexion : {}", historiqueConnexionDTO);

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
}
