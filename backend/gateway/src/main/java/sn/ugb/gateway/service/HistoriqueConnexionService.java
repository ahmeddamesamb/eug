package sn.ugb.gateway.service;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.service.dto.HistoriqueConnexionDTO;

/**
 * Service Interface for managing {@link sn.ugb.gateway.domain.HistoriqueConnexion}.
 */
public interface HistoriqueConnexionService {
    /**
     * Save a historiqueConnexion.
     *
     * @param historiqueConnexionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<HistoriqueConnexionDTO> save(HistoriqueConnexionDTO historiqueConnexionDTO);

    /**
     * Updates a historiqueConnexion.
     *
     * @param historiqueConnexionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<HistoriqueConnexionDTO> update(HistoriqueConnexionDTO historiqueConnexionDTO);

    /**
     * Partially updates a historiqueConnexion.
     *
     * @param historiqueConnexionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<HistoriqueConnexionDTO> partialUpdate(HistoriqueConnexionDTO historiqueConnexionDTO);

    /**
     * Get all the historiqueConnexions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HistoriqueConnexionDTO> findAll(Pageable pageable);

    /**
     * Returns the number of historiqueConnexions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of historiqueConnexions available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" historiqueConnexion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<HistoriqueConnexionDTO> findOne(Long id);

    /**
     * Delete the "id" historiqueConnexion.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the historiqueConnexion corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HistoriqueConnexionDTO> search(String query, Pageable pageable);
}
