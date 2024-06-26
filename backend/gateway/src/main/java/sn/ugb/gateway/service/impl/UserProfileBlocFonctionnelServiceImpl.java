package sn.ugb.gateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.repository.UserProfileBlocFonctionnelRepository;
import sn.ugb.gateway.repository.search.UserProfileBlocFonctionnelSearchRepository;
import sn.ugb.gateway.service.UserProfileBlocFonctionnelService;
import sn.ugb.gateway.service.dto.UserProfileBlocFonctionnelDTO;
import sn.ugb.gateway.service.mapper.UserProfileBlocFonctionnelMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gateway.domain.UserProfileBlocFonctionnel}.
 */
@Service
@Transactional
public class UserProfileBlocFonctionnelServiceImpl implements UserProfileBlocFonctionnelService {

    private final Logger log = LoggerFactory.getLogger(UserProfileBlocFonctionnelServiceImpl.class);

    private final UserProfileBlocFonctionnelRepository userProfileBlocFonctionnelRepository;

    private final UserProfileBlocFonctionnelMapper userProfileBlocFonctionnelMapper;

    private final UserProfileBlocFonctionnelSearchRepository userProfileBlocFonctionnelSearchRepository;

    public UserProfileBlocFonctionnelServiceImpl(
        UserProfileBlocFonctionnelRepository userProfileBlocFonctionnelRepository,
        UserProfileBlocFonctionnelMapper userProfileBlocFonctionnelMapper,
        UserProfileBlocFonctionnelSearchRepository userProfileBlocFonctionnelSearchRepository
    ) {
        this.userProfileBlocFonctionnelRepository = userProfileBlocFonctionnelRepository;
        this.userProfileBlocFonctionnelMapper = userProfileBlocFonctionnelMapper;
        this.userProfileBlocFonctionnelSearchRepository = userProfileBlocFonctionnelSearchRepository;
    }

    @Override
    public Mono<UserProfileBlocFonctionnelDTO> save(UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO) {
        log.debug("Request to save UserProfileBlocFonctionnel : {}", userProfileBlocFonctionnelDTO);
        return userProfileBlocFonctionnelRepository
            .save(userProfileBlocFonctionnelMapper.toEntity(userProfileBlocFonctionnelDTO))
            .flatMap(userProfileBlocFonctionnelSearchRepository::save)
            .map(userProfileBlocFonctionnelMapper::toDto);
    }

    @Override
    public Mono<UserProfileBlocFonctionnelDTO> update(UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO) {
        log.debug("Request to update UserProfileBlocFonctionnel : {}", userProfileBlocFonctionnelDTO);
        return userProfileBlocFonctionnelRepository
            .save(userProfileBlocFonctionnelMapper.toEntity(userProfileBlocFonctionnelDTO))
            .flatMap(userProfileBlocFonctionnelSearchRepository::save)
            .map(userProfileBlocFonctionnelMapper::toDto);
    }

    @Override
    public Mono<UserProfileBlocFonctionnelDTO> partialUpdate(UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO) {
        log.debug("Request to partially update UserProfileBlocFonctionnel : {}", userProfileBlocFonctionnelDTO);

        return userProfileBlocFonctionnelRepository
            .findById(userProfileBlocFonctionnelDTO.getId())
            .map(existingUserProfileBlocFonctionnel -> {
                userProfileBlocFonctionnelMapper.partialUpdate(existingUserProfileBlocFonctionnel, userProfileBlocFonctionnelDTO);

                return existingUserProfileBlocFonctionnel;
            })
            .flatMap(userProfileBlocFonctionnelRepository::save)
            .flatMap(savedUserProfileBlocFonctionnel -> {
                userProfileBlocFonctionnelSearchRepository.save(savedUserProfileBlocFonctionnel);
                return Mono.just(savedUserProfileBlocFonctionnel);
            })
            .map(userProfileBlocFonctionnelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserProfileBlocFonctionnelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserProfileBlocFonctionnels");
        return userProfileBlocFonctionnelRepository.findAllBy(pageable).map(userProfileBlocFonctionnelMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userProfileBlocFonctionnelRepository.count();
    }

    public Mono<Long> searchCount() {
        return userProfileBlocFonctionnelSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserProfileBlocFonctionnelDTO> findOne(Long id) {
        log.debug("Request to get UserProfileBlocFonctionnel : {}", id);
        return userProfileBlocFonctionnelRepository.findById(id).map(userProfileBlocFonctionnelMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserProfileBlocFonctionnel : {}", id);
        return userProfileBlocFonctionnelRepository.deleteById(id).then(userProfileBlocFonctionnelSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserProfileBlocFonctionnelDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserProfileBlocFonctionnels for query {}", query);
        return userProfileBlocFonctionnelSearchRepository.search(query, pageable).map(userProfileBlocFonctionnelMapper::toDto);
    }
}
