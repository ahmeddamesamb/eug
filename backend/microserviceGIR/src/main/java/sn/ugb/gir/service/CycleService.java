package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.CycleDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Cycle}.
 */
public interface CycleService {
    /**
     * Save a cycle.
     *
     * @param cycleDTO the entity to save.
     * @return the persisted entity.
     */
    CycleDTO save(CycleDTO cycleDTO);

    /**
     * Updates a cycle.
     *
     * @param cycleDTO the entity to update.
     * @return the persisted entity.
     */
    CycleDTO update(CycleDTO cycleDTO);

    /**
     * Partially updates a cycle.
     *
     * @param cycleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CycleDTO> partialUpdate(CycleDTO cycleDTO);

    /**
     * Get all the cycles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CycleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cycle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CycleDTO> findOne(Long id);

    /**
     * Delete the "id" cycle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cycle corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CycleDTO> search(String query, Pageable pageable);
}
