package sn.ugb.edt.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.edt.service.dto.PlanningDTO;

/**
 * Service Interface for managing {@link sn.ugb.edt.domain.Planning}.
 */
public interface PlanningService {
    /**
     * Save a planning.
     *
     * @param planningDTO the entity to save.
     * @return the persisted entity.
     */
    PlanningDTO save(PlanningDTO planningDTO);

    /**
     * Updates a planning.
     *
     * @param planningDTO the entity to update.
     * @return the persisted entity.
     */
    PlanningDTO update(PlanningDTO planningDTO);

    /**
     * Partially updates a planning.
     *
     * @param planningDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlanningDTO> partialUpdate(PlanningDTO planningDTO);

    /**
     * Get all the plannings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlanningDTO> findAll(Pageable pageable);

    /**
     * Get the "id" planning.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlanningDTO> findOne(Long id);

    /**
     * Delete the "id" planning.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the planning corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlanningDTO> search(String query, Pageable pageable);
}
