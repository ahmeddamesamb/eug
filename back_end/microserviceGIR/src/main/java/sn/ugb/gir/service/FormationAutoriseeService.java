package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.FormationAutoriseeDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.FormationAutorisee}.
 */
public interface FormationAutoriseeService {
    /**
     * Save a formationAutorisee.
     *
     * @param formationAutoriseeDTO the entity to save.
     * @return the persisted entity.
     */
    FormationAutoriseeDTO save(FormationAutoriseeDTO formationAutoriseeDTO);

    /**
     * Updates a formationAutorisee.
     *
     * @param formationAutoriseeDTO the entity to update.
     * @return the persisted entity.
     */
    FormationAutoriseeDTO update(FormationAutoriseeDTO formationAutoriseeDTO);

    /**
     * Partially updates a formationAutorisee.
     *
     * @param formationAutoriseeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormationAutoriseeDTO> partialUpdate(FormationAutoriseeDTO formationAutoriseeDTO);

    /**
     * Get all the formationAutorisees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormationAutoriseeDTO> findAll(Pageable pageable);

    /**
     * Get all the formationAutorisees with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormationAutoriseeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" formationAutorisee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormationAutoriseeDTO> findOne(Long id);

    /**
     * Delete the "id" formationAutorisee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
