package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.LyceeDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Lycee}.
 */
public interface LyceeService {
    /**
     * Save a lycee.
     *
     * @param lyceeDTO the entity to save.
     * @return the persisted entity.
     */
    LyceeDTO save(LyceeDTO lyceeDTO);

    /**
     * Updates a lycee.
     *
     * @param lyceeDTO the entity to update.
     * @return the persisted entity.
     */
    LyceeDTO update(LyceeDTO lyceeDTO);

    /**
     * Partially updates a lycee.
     *
     * @param lyceeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LyceeDTO> partialUpdate(LyceeDTO lyceeDTO);

    /**
     * Get all the lycees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LyceeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" lycee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LyceeDTO> findOne(Long id);

    /**
     * Delete the "id" lycee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the lycee corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LyceeDTO> search(String query, Pageable pageable);

    /**
     * Get all the lycees by a given Region.
     *
     * @param pageable the pagination information.
     * @param id the id of pagination
     *
     * @return the list of entities.
     */
    Page<LyceeDTO> findAllByRegionId(Pageable pageable, Long id);
}
