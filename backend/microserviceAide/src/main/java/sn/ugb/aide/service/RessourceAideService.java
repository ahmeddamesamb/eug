package sn.ugb.aide.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.aide.service.dto.RessourceAideDTO;

/**
 * Service Interface for managing {@link sn.ugb.aide.domain.RessourceAide}.
 */
public interface RessourceAideService {
    /**
     * Save a ressourceAide.
     *
     * @param ressourceAideDTO the entity to save.
     * @return the persisted entity.
     */
    RessourceAideDTO save(RessourceAideDTO ressourceAideDTO);

    /**
     * Updates a ressourceAide.
     *
     * @param ressourceAideDTO the entity to update.
     * @return the persisted entity.
     */
    RessourceAideDTO update(RessourceAideDTO ressourceAideDTO);

    /**
     * Partially updates a ressourceAide.
     *
     * @param ressourceAideDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RessourceAideDTO> partialUpdate(RessourceAideDTO ressourceAideDTO);

    /**
     * Get all the ressourceAides.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RessourceAideDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ressourceAide.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RessourceAideDTO> findOne(Long id);

    /**
     * Delete the "id" ressourceAide.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the ressourceAide corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RessourceAideDTO> search(String query, Pageable pageable);
}
