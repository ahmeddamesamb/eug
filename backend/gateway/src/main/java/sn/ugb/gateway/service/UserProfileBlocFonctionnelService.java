package sn.ugb.gateway.service;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.service.dto.UserProfileBlocFonctionnelDTO;

/**
 * Service Interface for managing {@link sn.ugb.gateway.domain.UserProfileBlocFonctionnel}.
 */
public interface UserProfileBlocFonctionnelService {
    /**
     * Save a userProfileBlocFonctionnel.
     *
     * @param userProfileBlocFonctionnelDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserProfileBlocFonctionnelDTO> save(UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO);

    /**
     * Updates a userProfileBlocFonctionnel.
     *
     * @param userProfileBlocFonctionnelDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserProfileBlocFonctionnelDTO> update(UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO);

    /**
     * Partially updates a userProfileBlocFonctionnel.
     *
     * @param userProfileBlocFonctionnelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserProfileBlocFonctionnelDTO> partialUpdate(UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO);

    /**
     * Get all the userProfileBlocFonctionnels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserProfileBlocFonctionnelDTO> findAll(Pageable pageable);

    /**
     * Returns the number of userProfileBlocFonctionnels available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of userProfileBlocFonctionnels available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" userProfileBlocFonctionnel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserProfileBlocFonctionnelDTO> findOne(Long id);

    /**
     * Delete the "id" userProfileBlocFonctionnel.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the userProfileBlocFonctionnel corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserProfileBlocFonctionnelDTO> search(String query, Pageable pageable);
}
