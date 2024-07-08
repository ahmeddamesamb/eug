package sn.ugb.gateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.domain.UserProfileBlocFonctionnel;
import sn.ugb.gateway.repository.UserProfileBlocFonctionnelRepository;
import sn.ugb.gateway.repository.search.UserProfileBlocFonctionnelSearchRepository;
import sn.ugb.gateway.service.UserProfileBlocFonctionnelService;
import sn.ugb.gateway.service.dto.ServiceUserDTO;
import sn.ugb.gateway.service.dto.UserProfileBlocFonctionnelDTO;
import sn.ugb.gateway.service.mapper.ServiceUserMapper;
import sn.ugb.gateway.service.mapper.UserProfileBlocFonctionnelMapper;
import sn.ugb.gateway.web.rest.errors.BadRequestAlertException;

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

    private final ServiceUserMapper serviceUserMapper;

    public UserProfileBlocFonctionnelServiceImpl(
        UserProfileBlocFonctionnelRepository userProfileBlocFonctionnelRepository,
        UserProfileBlocFonctionnelMapper userProfileBlocFonctionnelMapper,
        UserProfileBlocFonctionnelSearchRepository userProfileBlocFonctionnelSearchRepository,
        ServiceUserMapper serviceUserMapper
        ) {
        this.userProfileBlocFonctionnelRepository = userProfileBlocFonctionnelRepository;
        this.userProfileBlocFonctionnelMapper = userProfileBlocFonctionnelMapper;
        this.userProfileBlocFonctionnelSearchRepository = userProfileBlocFonctionnelSearchRepository;
        this.serviceUserMapper = serviceUserMapper;
    }

    @Override
    public Mono<UserProfileBlocFonctionnelDTO> save(UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO) {
        log.debug("Request to save UserProfileBlocFonctionnel : {}", userProfileBlocFonctionnelDTO);
        validateUserProfilBlocFonctionnel(userProfileBlocFonctionnelDTO);
        return userProfileBlocFonctionnelRepository
            .save(userProfileBlocFonctionnelMapper.toEntity(userProfileBlocFonctionnelDTO))
            .flatMap(userProfileBlocFonctionnelSearchRepository::save)
            .map(userProfileBlocFonctionnelMapper::toDto);
    }

    @Override
    public Mono<UserProfileBlocFonctionnelDTO> update(UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO) {
        log.debug("Request to update UserProfileBlocFonctionnel : {}", userProfileBlocFonctionnelDTO);
        validateUserProfilBlocFonctionnel(userProfileBlocFonctionnelDTO);
        return userProfileBlocFonctionnelRepository
            .save(userProfileBlocFonctionnelMapper.toEntity(userProfileBlocFonctionnelDTO))
            .flatMap(userProfileBlocFonctionnelSearchRepository::save)
            .map(userProfileBlocFonctionnelMapper::toDto);
    }

    @Override
    public Mono<UserProfileBlocFonctionnelDTO> partialUpdate(UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO) {
        log.debug("Request to partially update UserProfileBlocFonctionnel : {}", userProfileBlocFonctionnelDTO);
        validateUserProfilBlocFonctionnel(userProfileBlocFonctionnelDTO);
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

    @Override
    @Transactional(readOnly = true)
    public Flux<UserProfileBlocFonctionnelDTO> getAllUserProfilBlocFonctionnelByUserProfilId(Long userProfileId, Pageable pageable) {
        log.debug("Request to get all UserProfileBlocFonctionnel by UserProfileId : {}", userProfileId);
        return userProfileBlocFonctionnelRepository.findAllByUserProfileId(userProfileId, pageable)
            .map(userProfileBlocFonctionnelMapper::toDto);
    }

    @Override
    public Mono<Long> countByUserProfileId(Long userProfileId) {
        return userProfileBlocFonctionnelRepository.countByUserProfileId(userProfileId);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserProfileBlocFonctionnelDTO> findAllByBlocFonctionnelId(Long blocFonctionnelId, Pageable pageable) {
        return userProfileBlocFonctionnelRepository
            .findByBlocFonctionnelId(blocFonctionnelId, pageable)
            .map(userProfileBlocFonctionnelMapper::toDto);
    }

    @Override
    public Mono<Long> countByBlocFonctionnelId(Long blocFonctionnelId) {
        return userProfileBlocFonctionnelRepository.countByBlocFonctionnelId(blocFonctionnelId);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ServiceUserDTO> findAllServiceByUserProfileId(Long userProfileId, Pageable pageable) {
        return userProfileBlocFonctionnelRepository.findAllServiceByUserProfileId(userProfileId, pageable).map(serviceUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserProfileBlocFonctionnelDTO> findAllByEnCoursYN(Boolean enCoursYN, Pageable pageable) {
        return userProfileBlocFonctionnelRepository
            .findByEnCoursYN(enCoursYN, pageable)
            .map(userProfileBlocFonctionnelMapper::toDto);
    }

    @Override
    public Mono<Long> countByEnCoursYN(Boolean enCoursYN) {
        return userProfileBlocFonctionnelRepository.countByEnCoursYN(enCoursYN);
    }

    public void validateUserProfilBlocFonctionnel(UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO) {
        // RG1: Tous les champs sont obligatoires
        if (userProfileBlocFonctionnelDTO.getBlocFonctionnel() == null || userProfileBlocFonctionnelDTO.getUserProfil() == null || userProfileBlocFonctionnelDTO.getDate() == null || userProfileBlocFonctionnelDTO.getEnCoursYN() == null) {
            throw new BadRequestAlertException("Tous les champs sont obligatoires", "UserProfileBlocFonctionnel", "missingFields");
        }

        // RG2: L'utilisateur doit d'abord avoir un profil actif
        if (!userProfileBlocFonctionnelDTO.getUserProfil().getProfil().getActifYN()) {
            throw new BadRequestAlertException("L'utilisateur doit avoir un profil actif", "UserProfileBlocFonctionnel", "inactiveProfile");
        }

        // RG4: Si le bloc est associé à un utilisateur alors le champ En cours reçoit 1
        userProfileBlocFonctionnelDTO.setEnCoursYN(true);
    }
}
