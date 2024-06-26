package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.TypeHandicapDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.TypeHandicap}.
 */
public interface TypeHandicapService {
    /**
     * Save a typeHandicap.
     *
     * @param typeHandicapDTO the entity to save.
     * @return the persisted entity.
     */
    TypeHandicapDTO save(TypeHandicapDTO typeHandicapDTO);

    /**
     * Updates a typeHandicap.
     *
     * @param typeHandicapDTO the entity to update.
     * @return the persisted entity.
     */
    TypeHandicapDTO update(TypeHandicapDTO typeHandicapDTO);

    /**
     * Partially updates a typeHandicap.
     *
     * @param typeHandicapDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeHandicapDTO> partialUpdate(TypeHandicapDTO typeHandicapDTO);

    /**
     * Get all the typeHandicaps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeHandicapDTO> findAll(Pageable pageable);

    /**
     * Get the "id" typeHandicap.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeHandicapDTO> findOne(Long id);

    /**
     * Delete the "id" typeHandicap.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the typeHandicap corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeHandicapDTO> search(String query, Pageable pageable);
}
