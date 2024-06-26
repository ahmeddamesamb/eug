package sn.ugb.gp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gp.service.dto.EnseignementDTO;

/**
 * Service Interface for managing {@link sn.ugb.gp.domain.Enseignement}.
 */
public interface EnseignementService {
    /**
     * Save a enseignement.
     *
     * @param enseignementDTO the entity to save.
     * @return the persisted entity.
     */
    EnseignementDTO save(EnseignementDTO enseignementDTO);

    /**
     * Updates a enseignement.
     *
     * @param enseignementDTO the entity to update.
     * @return the persisted entity.
     */
    EnseignementDTO update(EnseignementDTO enseignementDTO);

    /**
     * Partially updates a enseignement.
     *
     * @param enseignementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EnseignementDTO> partialUpdate(EnseignementDTO enseignementDTO);

    /**
     * Get all the enseignements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EnseignementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" enseignement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EnseignementDTO> findOne(Long id);

    /**
     * Delete the "id" enseignement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the enseignement corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EnseignementDTO> search(String query, Pageable pageable);
}
