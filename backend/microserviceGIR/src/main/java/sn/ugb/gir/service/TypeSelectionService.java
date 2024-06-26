package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.TypeSelectionDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.TypeSelection}.
 */
public interface TypeSelectionService {
    /**
     * Save a typeSelection.
     *
     * @param typeSelectionDTO the entity to save.
     * @return the persisted entity.
     */
    TypeSelectionDTO save(TypeSelectionDTO typeSelectionDTO);

    /**
     * Updates a typeSelection.
     *
     * @param typeSelectionDTO the entity to update.
     * @return the persisted entity.
     */
    TypeSelectionDTO update(TypeSelectionDTO typeSelectionDTO);

    /**
     * Partially updates a typeSelection.
     *
     * @param typeSelectionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeSelectionDTO> partialUpdate(TypeSelectionDTO typeSelectionDTO);

    /**
     * Get all the typeSelections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeSelectionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" typeSelection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeSelectionDTO> findOne(Long id);

    /**
     * Delete the "id" typeSelection.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the typeSelection corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeSelectionDTO> search(String query, Pageable pageable);
}
