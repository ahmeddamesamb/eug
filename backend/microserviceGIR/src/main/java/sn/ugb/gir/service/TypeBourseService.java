package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.TypeBourseDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.TypeBourse}.
 */
public interface TypeBourseService {
    /**
     * Save a typeBourse.
     *
     * @param typeBourseDTO the entity to save.
     * @return the persisted entity.
     */
    TypeBourseDTO save(TypeBourseDTO typeBourseDTO);

    /**
     * Updates a typeBourse.
     *
     * @param typeBourseDTO the entity to update.
     * @return the persisted entity.
     */
    TypeBourseDTO update(TypeBourseDTO typeBourseDTO);

    /**
     * Partially updates a typeBourse.
     *
     * @param typeBourseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeBourseDTO> partialUpdate(TypeBourseDTO typeBourseDTO);

    /**
     * Get all the typeBourses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeBourseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" typeBourse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeBourseDTO> findOne(Long id);

    /**
     * Delete the "id" typeBourse.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the typeBourse corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeBourseDTO> search(String query, Pageable pageable);
}
