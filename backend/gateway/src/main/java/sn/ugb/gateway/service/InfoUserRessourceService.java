package sn.ugb.gateway.service;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.service.dto.InfoUserRessourceDTO;

/**
 * Service Interface for managing {@link sn.ugb.gateway.domain.InfoUserRessource}.
 */
public interface InfoUserRessourceService {
    /**
     * Save a infoUserRessource.
     *
     * @param infoUserRessourceDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InfoUserRessourceDTO> save(InfoUserRessourceDTO infoUserRessourceDTO);

    /**
     * Updates a infoUserRessource.
     *
     * @param infoUserRessourceDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InfoUserRessourceDTO> update(InfoUserRessourceDTO infoUserRessourceDTO);

    /**
     * Partially updates a infoUserRessource.
     *
     * @param infoUserRessourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InfoUserRessourceDTO> partialUpdate(InfoUserRessourceDTO infoUserRessourceDTO);

    /**
     * Get all the infoUserRessources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<InfoUserRessourceDTO> findAll(Pageable pageable);

    /**
     * Returns the number of infoUserRessources available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of infoUserRessources available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" infoUserRessource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InfoUserRessourceDTO> findOne(Long id);

    /**
     * Delete the "id" infoUserRessource.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the infoUserRessource corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<InfoUserRessourceDTO> search(String query, Pageable pageable);
}
