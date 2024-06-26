package sn.ugb.gateway.service;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.service.dto.ServiceUserDTO;

/**
 * Service Interface for managing {@link sn.ugb.gateway.domain.ServiceUser}.
 */
public interface ServiceUserService {
    /**
     * Save a serviceUser.
     *
     * @param serviceUserDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ServiceUserDTO> save(ServiceUserDTO serviceUserDTO);

    /**
     * Updates a serviceUser.
     *
     * @param serviceUserDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ServiceUserDTO> update(ServiceUserDTO serviceUserDTO);

    /**
     * Partially updates a serviceUser.
     *
     * @param serviceUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ServiceUserDTO> partialUpdate(ServiceUserDTO serviceUserDTO);

    /**
     * Get all the serviceUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ServiceUserDTO> findAll(Pageable pageable);

    /**
     * Returns the number of serviceUsers available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of serviceUsers available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" serviceUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ServiceUserDTO> findOne(Long id);

    /**
     * Delete the "id" serviceUser.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the serviceUser corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ServiceUserDTO> search(String query, Pageable pageable);
}
