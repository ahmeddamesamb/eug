package sn.ugb.grh.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.grh.service.dto.EnseignantDTO;

/**
 * Service Interface for managing {@link sn.ugb.grh.domain.Enseignant}.
 */
public interface EnseignantService {
    /**
     * Save a enseignant.
     *
     * @param enseignantDTO the entity to save.
     * @return the persisted entity.
     */
    EnseignantDTO save(EnseignantDTO enseignantDTO);

    /**
     * Updates a enseignant.
     *
     * @param enseignantDTO the entity to update.
     * @return the persisted entity.
     */
    EnseignantDTO update(EnseignantDTO enseignantDTO);

    /**
     * Partially updates a enseignant.
     *
     * @param enseignantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EnseignantDTO> partialUpdate(EnseignantDTO enseignantDTO);

    /**
     * Get all the enseignants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EnseignantDTO> findAll(Pageable pageable);

    /**
     * Get the "id" enseignant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EnseignantDTO> findOne(Long id);

    /**
     * Delete the "id" enseignant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the enseignant corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EnseignantDTO> search(String query, Pageable pageable);
}
