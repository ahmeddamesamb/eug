package sn.ugb.gateway.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.service.dto.RessourceDTO;
import sn.ugb.gateway.service.dto.UserProfileDTO;

import java.util.Optional;

/**
 * Service Interface for managing {@link sn.ugb.gateway.domain.UserProfile}.
 */
public interface UserProfileService {
    /**
     * Save a userProfile.
     *
     * @param userProfileDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserProfileDTO> save(UserProfileDTO userProfileDTO);

    /**
     * Updates a userProfile.
     *
     * @param userProfileDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserProfileDTO> update(UserProfileDTO userProfileDTO);

    /**
     * Partially updates a userProfile.
     *
     * @param userProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserProfileDTO> partialUpdate(UserProfileDTO userProfileDTO);

    /**
     * Get all the userProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserProfileDTO> findAll(Pageable pageable);

    /**
     * Returns the number of userProfiles available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of userProfiles available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" userProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserProfileDTO> findOne(Long id);

    /**
     * Delete the "id" userProfile.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the userProfile corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserProfileDTO> search(String query, Pageable pageable);

    @Transactional(readOnly = true)
    Flux<UserProfileDTO> getAllUserProfilByProfilId(Long profilId, Pageable pageable);

    Flux<UserProfileDTO> getAllUserProfilByInfosUserId(Long infoUserId, Pageable pageable);

    Mono<UserProfileDTO> archiveUserProfil(Long id);

    Flux<UserProfileDTO> getAllUserProfilByEncoursYN(Boolean enCoursYN, Pageable pageable);

    Mono<Long> getAllUserProfilByInfosUserIdCount(Long infoUserId);

    Mono<Long> getAllUserProfilByEncoursYNCount(Boolean enCoursYN);

    Mono<Long> getAllUserProfilByProfilIdCount(Long profilId);
}
