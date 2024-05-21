package sn.ugb.aide.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.aide.service.dto.RessourceDTO;

/**
 * Service Interface for managing {@link sn.ugb.aide.domain.Ressource}.
 */
public interface RessourceService {
    /**
     * Save a ressource.
     *
     * @param ressourceDTO the entity to save.
     * @return the persisted entity.
     */
    RessourceDTO save(RessourceDTO ressourceDTO);

    /**
     * Updates a ressource.
     *
     * @param ressourceDTO the entity to update.
     * @return the persisted entity.
     */
    RessourceDTO update(RessourceDTO ressourceDTO);

    /**
     * Partially updates a ressource.
     *
     * @param ressourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RessourceDTO> partialUpdate(RessourceDTO ressourceDTO);

    /**
     * Get all the ressources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RessourceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ressource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RessourceDTO> findOne(Long id);

    /**
     * Delete the "id" ressource.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
