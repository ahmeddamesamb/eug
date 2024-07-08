package sn.ugb.gateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.domain.InfosUser;
import sn.ugb.gateway.domain.User;
import sn.ugb.gateway.repository.InfosUserRepository;
import sn.ugb.gateway.repository.UserRepository;
import sn.ugb.gateway.repository.search.InfosUserSearchRepository;
import sn.ugb.gateway.service.InfosUserService;
import sn.ugb.gateway.service.dto.InfosUserDTO;
import sn.ugb.gateway.service.dto.UserDTO;
import sn.ugb.gateway.service.mapper.InfosUserMapper;
import sn.ugb.gateway.web.rest.errors.BadRequestAlertException;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Service Implementation for managing {@link sn.ugb.gateway.domain.InfosUser}.
 */
@Service
@Transactional
public class InfosUserServiceImpl implements InfosUserService {

    private final Logger log = LoggerFactory.getLogger(InfosUserServiceImpl.class);

    private static final String ENTITY_NAME = "InfosUser";


    private final InfosUserRepository infosUserRepository;

    private final InfosUserMapper infosUserMapper;

    private final InfosUserSearchRepository infosUserSearchRepository;

    @Autowired
    private UserRepository userRepository;

    public InfosUserServiceImpl(
        InfosUserRepository infosUserRepository,
        InfosUserMapper infosUserMapper,
        InfosUserSearchRepository infosUserSearchRepository
    ) {
        this.infosUserRepository = infosUserRepository;
        this.infosUserMapper = infosUserMapper;
        this.infosUserSearchRepository = infosUserSearchRepository;
    }

    @Override
    public Mono<InfosUserDTO> save(InfosUserDTO infosUserDTO) {
        log.debug("Request to save InfosUser : {}", infosUserDTO);

        return validateInfosUser(infosUserDTO)
            .then(infosUserRepository.save(infosUserMapper.toEntity(infosUserDTO)))
            .flatMap(infosUserSearchRepository::save)
            .map(infosUserMapper::toDto);
    }

    @Override
    public Mono<InfosUserDTO> update(InfosUserDTO infosUserDTO) {
        log.debug("Request to update InfosUser : {}", infosUserDTO);

        return validateInfosUser(infosUserDTO)
            .then(infosUserRepository.save(infosUserMapper.toEntity(infosUserDTO)))
            .flatMap(infosUserSearchRepository::save)
            .map(infosUserMapper::toDto);
    }

    @Override
    public Mono<InfosUserDTO> partialUpdate(InfosUserDTO infosUserDTO) {
        log.debug("Request to partially update InfosUser : {}", infosUserDTO);

        validateInfosUser(infosUserDTO);
        return infosUserRepository
            .findById(infosUserDTO.getId())
            .map(existingInfosUser -> {
                infosUserMapper.partialUpdate(existingInfosUser, infosUserDTO);

                return existingInfosUser;
            })
            .flatMap(infosUserRepository::save)
            .flatMap(savedInfosUser -> {
                infosUserSearchRepository.save(savedInfosUser);
                return Mono.just(savedInfosUser);
            })
            .map(infosUserMapper::toDto);
    }

    private Mono<Void> validateInfosUser(InfosUserDTO infosUserDTO) {
        if (infosUserDTO.getDateAjout() == null) {
            return Mono.error(new BadRequestAlertException("DateAjout est obligatoire", ENTITY_NAME, "dateajoutnull"));
        }
        if (infosUserDTO.getActifYN() == null) {
            return Mono.error(new BadRequestAlertException("ActifYN est obligatoire", ENTITY_NAME, "actifynnull"));
        }
        if (infosUserDTO.getUser() == null || infosUserDTO.getUser().getLogin() == null) {
            return Mono.error(new BadRequestAlertException("Le login de l'utilisateur est obligatoire", ENTITY_NAME, "userloginnull"));
        }

        String email = infosUserDTO.getUser().getLogin();
        String emailPattern = "^[a-z]+\\.[a-z]+\\.[0-9]+@ugb\\.edu\\.sn$";

        if (!Pattern.matches(emailPattern, email)) {
            return Mono.error(new BadRequestAlertException("Format d'email invalide", ENTITY_NAME, "invalidemail"));
        }

        return userRepository.findById(infosUserDTO.getUser().getId())
            .flatMap(user -> {
                user.setActivated(infosUserDTO.getActifYN());
                return userRepository.save(user);
            })
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Utilisateur non trouvé", ENTITY_NAME, "usernotfound")))
            .then();
    }


    @Override
    @Transactional(readOnly = true)
    public Flux<InfosUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InfosUsers");
        return infosUserRepository.findAllBy(pageable).map(infosUserMapper::toDto);
    }

    public Flux<InfosUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return infosUserRepository.findAllWithEagerRelationships(pageable).map(infosUserMapper::toDto);
    }

    public Mono<Long> countAll() {
        return infosUserRepository.count();
    }

    public Mono<Long> searchCount() {
        return infosUserSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<InfosUserDTO> findOne(Long id) {
        log.debug("Request to get InfosUser : {}", id);
        return infosUserRepository.findOneWithEagerRelationships(id).map(infosUserMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete InfosUser : {}", id);
        return infosUserRepository.deleteById(id).then(infosUserSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<InfosUserDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InfosUsers for query {}", query);
        return infosUserSearchRepository.search(query, pageable).map(infosUserMapper::toDto);
    }





    /**
     * @param id
     * @param <Void>
     * @return
     */

    @Transactional
    @Override
    public Mono<Void> archiveInfosUser(Long id) {
        return infosUserRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "InfosUser introuvable")))
            .flatMap(infosUser -> {
                infosUser.setActifYN(false);
                if (infosUser.getUserId() != null) {
                    return userRepository.findById(infosUser.getUserId())
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable")))
                        .flatMap(user -> {
                            log.debug("Désactivation de l'utilisateur: {}", user.getLogin());
                            user.setActivated(false);
                            return userRepository.save(user)
                                .doOnSuccess(savedUser -> log.debug("Utilisateur désactivé: {}", savedUser.getLogin()))
                                .then(infosUserRepository.save(infosUser))
                                .doOnSuccess(savedInfosUser -> log.debug("InfosUser archivé: {}", savedInfosUser.getId()))
                                .then();
                        });
                }
                return infosUserRepository.save(infosUser)
                    .doOnSuccess(savedInfosUser -> log.debug("InfosUser archivé: {}", savedInfosUser.getId()))
                    .then();
            });
    }


    @Override
    public Flux<InfosUserDTO> findAllByActifYN(Boolean actifYN) {
        Flux<InfosUser> infosUsers = (Flux<InfosUser>) infosUserRepository.findAllByActifYN(actifYN);
        return infosUsers.map(infosUserMapper::toDto);
    }
}
