package sn.ugb.gateway.service;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.service.dto.ProfilDTO;

/**
 * Service Interface for managing {@link sn.ugb.gateway.domain.Profil}.
 */
public interface ProfilService {
    /**
     * Save a profil.
     *
     * @param profilDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ProfilDTO> save(ProfilDTO profilDTO);

    /**
     * Updates a profil.
     *
     * @param profilDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ProfilDTO> update(ProfilDTO profilDTO);

    /**
     * Partially updates a profil.
     *
     * @param profilDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ProfilDTO> partialUpdate(ProfilDTO profilDTO);

    /**
     * Get all the profils.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ProfilDTO> findAll(Pageable pageable);

    /**
     * Returns the number of profils available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of profils available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" profil.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ProfilDTO> findOne(Long id);

    /**
     * Delete the "id" profil.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the profil corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ProfilDTO> search(String query, Pageable pageable);
}
