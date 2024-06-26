package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.TypeFraisDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.TypeFrais}.
 */
public interface TypeFraisService {
    /**
     * Save a typeFrais.
     *
     * @param typeFraisDTO the entity to save.
     * @return the persisted entity.
     */
    TypeFraisDTO save(TypeFraisDTO typeFraisDTO);

    /**
     * Updates a typeFrais.
     *
     * @param typeFraisDTO the entity to update.
     * @return the persisted entity.
     */
    TypeFraisDTO update(TypeFraisDTO typeFraisDTO);

    /**
     * Partially updates a typeFrais.
     *
     * @param typeFraisDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeFraisDTO> partialUpdate(TypeFraisDTO typeFraisDTO);

    /**
     * Get all the typeFrais.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeFraisDTO> findAll(Pageable pageable);

    /**
     * Get the "id" typeFrais.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeFraisDTO> findOne(Long id);

    /**
     * Delete the "id" typeFrais.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the typeFrais corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeFraisDTO> search(String query, Pageable pageable);
}
