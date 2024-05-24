package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.FormationPriveeDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.FormationPrivee}.
 */
public interface FormationPriveeService {
    /**
     * Save a formationPrivee.
     *
     * @param formationPriveeDTO the entity to save.
     * @return the persisted entity.
     */
    FormationPriveeDTO save(FormationPriveeDTO formationPriveeDTO);

    /**
     * Updates a formationPrivee.
     *
     * @param formationPriveeDTO the entity to update.
     * @return the persisted entity.
     */
    FormationPriveeDTO update(FormationPriveeDTO formationPriveeDTO);

    /**
     * Partially updates a formationPrivee.
     *
     * @param formationPriveeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormationPriveeDTO> partialUpdate(FormationPriveeDTO formationPriveeDTO);

    /**
     * Get all the formationPrivees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormationPriveeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" formationPrivee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormationPriveeDTO> findOne(Long id);

    /**
     * Delete the "id" formationPrivee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
