package sn.ugb.gateway.service;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.service.dto.BlocFonctionnelDTO;

/**
 * Service Interface for managing {@link sn.ugb.gateway.domain.BlocFonctionnel}.
 */
public interface BlocFonctionnelService {
    /**
     * Save a blocFonctionnel.
     *
     * @param blocFonctionnelDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<BlocFonctionnelDTO> save(BlocFonctionnelDTO blocFonctionnelDTO);

    /**
     * Updates a blocFonctionnel.
     *
     * @param blocFonctionnelDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<BlocFonctionnelDTO> update(BlocFonctionnelDTO blocFonctionnelDTO);

    /**
     * Partially updates a blocFonctionnel.
     *
     * @param blocFonctionnelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<BlocFonctionnelDTO> partialUpdate(BlocFonctionnelDTO blocFonctionnelDTO);

    /**
     * Get all the blocFonctionnels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<BlocFonctionnelDTO> findAll(Pageable pageable);

    /**
     * Returns the number of blocFonctionnels available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of blocFonctionnels available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" blocFonctionnel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<BlocFonctionnelDTO> findOne(Long id);

    /**
     * Delete the "id" blocFonctionnel.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the blocFonctionnel corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<BlocFonctionnelDTO> search(String query, Pageable pageable);
}
