package sn.ugb.gateway.service;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.service.dto.InfosUserDTO;

/**
 * Service Interface for managing {@link sn.ugb.gateway.domain.InfosUser}.
 */
public interface InfosUserService {
    /**
     * Save a infosUser.
     *
     * @param infosUserDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InfosUserDTO> save(InfosUserDTO infosUserDTO);

    /**
     * Updates a infosUser.
     *
     * @param infosUserDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InfosUserDTO> update(InfosUserDTO infosUserDTO);

    /**
     * Partially updates a infosUser.
     *
     * @param infosUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InfosUserDTO> partialUpdate(InfosUserDTO infosUserDTO);

    /**
     * Get all the infosUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<InfosUserDTO> findAll(Pageable pageable);

    /**
     * Get all the infosUsers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<InfosUserDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of infosUsers available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of infosUsers available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" infosUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InfosUserDTO> findOne(Long id);

    /**
     * Delete the "id" infosUser.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the infosUser corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<InfosUserDTO> search(String query, Pageable pageable);
}
