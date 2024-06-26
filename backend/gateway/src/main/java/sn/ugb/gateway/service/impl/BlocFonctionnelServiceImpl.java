package sn.ugb.gateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.repository.BlocFonctionnelRepository;
import sn.ugb.gateway.repository.search.BlocFonctionnelSearchRepository;
import sn.ugb.gateway.service.BlocFonctionnelService;
import sn.ugb.gateway.service.dto.BlocFonctionnelDTO;
import sn.ugb.gateway.service.mapper.BlocFonctionnelMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gateway.domain.BlocFonctionnel}.
 */
@Service
@Transactional
public class BlocFonctionnelServiceImpl implements BlocFonctionnelService {

    private final Logger log = LoggerFactory.getLogger(BlocFonctionnelServiceImpl.class);

    private final BlocFonctionnelRepository blocFonctionnelRepository;

    private final BlocFonctionnelMapper blocFonctionnelMapper;

    private final BlocFonctionnelSearchRepository blocFonctionnelSearchRepository;

    public BlocFonctionnelServiceImpl(
        BlocFonctionnelRepository blocFonctionnelRepository,
        BlocFonctionnelMapper blocFonctionnelMapper,
        BlocFonctionnelSearchRepository blocFonctionnelSearchRepository
    ) {
        this.blocFonctionnelRepository = blocFonctionnelRepository;
        this.blocFonctionnelMapper = blocFonctionnelMapper;
        this.blocFonctionnelSearchRepository = blocFonctionnelSearchRepository;
    }

    @Override
    public Mono<BlocFonctionnelDTO> save(BlocFonctionnelDTO blocFonctionnelDTO) {
        log.debug("Request to save BlocFonctionnel : {}", blocFonctionnelDTO);
        return blocFonctionnelRepository
            .save(blocFonctionnelMapper.toEntity(blocFonctionnelDTO))
            .flatMap(blocFonctionnelSearchRepository::save)
            .map(blocFonctionnelMapper::toDto);
    }

    @Override
    public Mono<BlocFonctionnelDTO> update(BlocFonctionnelDTO blocFonctionnelDTO) {
        log.debug("Request to update BlocFonctionnel : {}", blocFonctionnelDTO);
        return blocFonctionnelRepository
            .save(blocFonctionnelMapper.toEntity(blocFonctionnelDTO))
            .flatMap(blocFonctionnelSearchRepository::save)
            .map(blocFonctionnelMapper::toDto);
    }

    @Override
    public Mono<BlocFonctionnelDTO> partialUpdate(BlocFonctionnelDTO blocFonctionnelDTO) {
        log.debug("Request to partially update BlocFonctionnel : {}", blocFonctionnelDTO);

        return blocFonctionnelRepository
            .findById(blocFonctionnelDTO.getId())
            .map(existingBlocFonctionnel -> {
                blocFonctionnelMapper.partialUpdate(existingBlocFonctionnel, blocFonctionnelDTO);

                return existingBlocFonctionnel;
            })
            .flatMap(blocFonctionnelRepository::save)
            .flatMap(savedBlocFonctionnel -> {
                blocFonctionnelSearchRepository.save(savedBlocFonctionnel);
                return Mono.just(savedBlocFonctionnel);
            })
            .map(blocFonctionnelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BlocFonctionnelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BlocFonctionnels");
        return blocFonctionnelRepository.findAllBy(pageable).map(blocFonctionnelMapper::toDto);
    }

    public Mono<Long> countAll() {
        return blocFonctionnelRepository.count();
    }

    public Mono<Long> searchCount() {
        return blocFonctionnelSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<BlocFonctionnelDTO> findOne(Long id) {
        log.debug("Request to get BlocFonctionnel : {}", id);
        return blocFonctionnelRepository.findById(id).map(blocFonctionnelMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete BlocFonctionnel : {}", id);
        return blocFonctionnelRepository.deleteById(id).then(blocFonctionnelSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BlocFonctionnelDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BlocFonctionnels for query {}", query);
        return blocFonctionnelSearchRepository.search(query, pageable).map(blocFonctionnelMapper::toDto);
    }
}
