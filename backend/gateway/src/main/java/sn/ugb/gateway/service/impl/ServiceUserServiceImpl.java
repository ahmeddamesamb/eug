package sn.ugb.gateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.repository.RessourceRepository;
import sn.ugb.gateway.repository.ServiceUserRepository;
import sn.ugb.gateway.repository.UserProfileRepository;
import sn.ugb.gateway.repository.search.ServiceUserSearchRepository;
import sn.ugb.gateway.service.ServiceUserService;
import sn.ugb.gateway.service.dto.ServiceUserDTO;
import sn.ugb.gateway.service.mapper.ServiceUserMapper;


/**
 * Service Implementation for managing {@link sn.ugb.gateway.domain.ServiceUser}.
 */
@Service
@Transactional
public class ServiceUserServiceImpl implements ServiceUserService {

    private final Logger log = LoggerFactory.getLogger(ServiceUserServiceImpl.class);

    private final ServiceUserRepository serviceUserRepository;

    private final ServiceUserMapper serviceUserMapper;

    private final ServiceUserSearchRepository serviceUserSearchRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RessourceRepository ressourceRepository;


    public ServiceUserServiceImpl(
        ServiceUserRepository serviceUserRepository,
        ServiceUserMapper serviceUserMapper,
        ServiceUserSearchRepository serviceUserSearchRepository
    ) {
        this.serviceUserRepository = serviceUserRepository;
        this.serviceUserMapper = serviceUserMapper;
        this.serviceUserSearchRepository = serviceUserSearchRepository;
    }

    @Override
    public Mono<ServiceUserDTO> save(ServiceUserDTO serviceUserDTO) {
        log.debug("Request to save ServiceUser : {}", serviceUserDTO);

        validateServiceUser(serviceUserDTO);

        return serviceUserRepository
            .save(serviceUserMapper.toEntity(serviceUserDTO))
            .flatMap(serviceUserSearchRepository::save)
            .map(serviceUserMapper::toDto);
    }

    @Override
    public Mono<ServiceUserDTO> update(ServiceUserDTO serviceUserDTO) {
        log.debug("Request to update ServiceUser : {}", serviceUserDTO);

        validateServiceUser(serviceUserDTO);

        return serviceUserRepository
            .save(serviceUserMapper.toEntity(serviceUserDTO))
            .flatMap(serviceUserSearchRepository::save)
            .map(serviceUserMapper::toDto);
    }

    @Override
    public Mono<ServiceUserDTO> partialUpdate(ServiceUserDTO serviceUserDTO) {
        log.debug("Request to partially update ServiceUser : {}", serviceUserDTO);

        return serviceUserRepository
            .findById(serviceUserDTO.getId())
            .map(existingServiceUser -> {
                serviceUserMapper.partialUpdate(existingServiceUser, serviceUserDTO);

                return existingServiceUser;
            })
            .flatMap(serviceUserRepository::save)
            .flatMap(savedServiceUser -> {
                serviceUserSearchRepository.save(savedServiceUser);
                return Mono.just(savedServiceUser);
            })
            .map(serviceUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ServiceUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceUsers");
        return serviceUserRepository.findAllBy(pageable).map(serviceUserMapper::toDto);
    }

    public Mono<Long> countAll() {
        return serviceUserRepository.count();
    }

    public Mono<Long> searchCount() {
        return serviceUserSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ServiceUserDTO> findOne(Long id) {
        log.debug("Request to get ServiceUser : {}", id);
        return serviceUserRepository.findById(id).map(serviceUserMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ServiceUser : {}", id);
        return serviceUserRepository.deleteById(id).then(serviceUserSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ServiceUserDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceUsers for query {}", query);
        return serviceUserSearchRepository.search(query, pageable).map(serviceUserMapper::toDto);
    }



    private void validateServiceUser(ServiceUserDTO serviceUserDTO) {
        if (serviceUserDTO == null) {
            throw new IllegalArgumentException("Le ServiceUserDTO ne peut pas être null");
        }

        if (isEmpty(serviceUserDTO.getNom())) {
            throw new IllegalArgumentException("Le champ 'Nom' est obligatoire");
        }

        if ((serviceUserDTO.getDateAjout()) == null) {
            throw new IllegalArgumentException("Le champ 'DateAjout' est obligatoire");
        }

        validateActifYN(serviceUserDTO);
    }

    private void validateActifYN(ServiceUserDTO serviceUserDTO) {
        if (serviceUserDTO.getActifYN() == null) {
            throw new IllegalArgumentException("Le champ 'ActifYN' est obligatoire");
        }

        // RG2: Définir ActifYN à true si le service est ajouté (nouvelle entité)
        if (serviceUserDTO.getId() == null) {
            serviceUserDTO.setActifYN(true);
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    public Mono<ServiceUserDTO> setServiceUserActifYN(Long id, Boolean actifYN) {
        log.debug("Request to set actifYN for ServiceUser : {} to {}", id, actifYN);
        return serviceUserRepository.findById(id)
            .flatMap(serviceUser -> {
                serviceUser.setActifYN(actifYN);
                return serviceUserRepository.save(serviceUser);
            })
            .flatMap(serviceUserSearchRepository::save)
            .map(serviceUserMapper::toDto);
    }



}
