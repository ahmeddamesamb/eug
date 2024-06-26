package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.FormationInvalideDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.FormationInvalide}.
 */
public interface FormationInvalideService {
    /**
     * Save a formationInvalide.
     *
     * @param formationInvalideDTO the entity to save.
     * @return the persisted entity.
     */
    FormationInvalideDTO save(FormationInvalideDTO formationInvalideDTO);

    /**
     * Updates a formationInvalide.
     *
     * @param formationInvalideDTO the entity to update.
     * @return the persisted entity.
     */
    FormationInvalideDTO update(FormationInvalideDTO formationInvalideDTO);

    /**
     * Partially updates a formationInvalide.
     *
     * @param formationInvalideDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormationInvalideDTO> partialUpdate(FormationInvalideDTO formationInvalideDTO);

    /**
     * Get all the formationInvalides.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormationInvalideDTO> findAll(Pageable pageable);

    /**
     * Get the "id" formationInvalide.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormationInvalideDTO> findOne(Long id);

    /**
     * Delete the "id" formationInvalide.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the formationInvalide corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormationInvalideDTO> search(String query, Pageable pageable);
}
