package sn.ugb.gateway.service;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.service.dto.RessourceDTO;

/**
 * Service Interface for managing {@link sn.ugb.gateway.domain.Ressource}.
 */
public interface RessourceService {
    /**
     * Save a ressource.
     *
     * @param ressourceDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<RessourceDTO> save(RessourceDTO ressourceDTO);

    /**
     * Updates a ressource.
     *
     * @param ressourceDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<RessourceDTO> update(RessourceDTO ressourceDTO);

    /**
     * Partially updates a ressource.
     *
     * @param ressourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<RessourceDTO> partialUpdate(RessourceDTO ressourceDTO);

    /**
     * Get all the ressources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<RessourceDTO> findAll(Pageable pageable);

    /**
     * Returns the number of ressources available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of ressources available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" ressource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<RessourceDTO> findOne(Long id);

    /**
     * Delete the "id" ressource.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the ressource corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<RessourceDTO> search(String query, Pageable pageable);
}
