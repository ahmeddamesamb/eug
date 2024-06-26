package sn.ugb.gd.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gd.service.dto.TypeRapportDTO;

/**
 * Service Interface for managing {@link sn.ugb.gd.domain.TypeRapport}.
 */
public interface TypeRapportService {
    /**
     * Save a typeRapport.
     *
     * @param typeRapportDTO the entity to save.
     * @return the persisted entity.
     */
    TypeRapportDTO save(TypeRapportDTO typeRapportDTO);

    /**
     * Updates a typeRapport.
     *
     * @param typeRapportDTO the entity to update.
     * @return the persisted entity.
     */
    TypeRapportDTO update(TypeRapportDTO typeRapportDTO);

    /**
     * Partially updates a typeRapport.
     *
     * @param typeRapportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeRapportDTO> partialUpdate(TypeRapportDTO typeRapportDTO);

    /**
     * Get all the typeRapports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeRapportDTO> findAll(Pageable pageable);

    /**
     * Get the "id" typeRapport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeRapportDTO> findOne(Long id);

    /**
     * Delete the "id" typeRapport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the typeRapport corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeRapportDTO> search(String query, Pageable pageable);
}
