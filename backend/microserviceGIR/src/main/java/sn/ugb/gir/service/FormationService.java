package sn.ugb.gir.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.FormationDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Formation}.
 */
public interface FormationService {
    /**
     * Save a formation.
     *
     * @param formationDTO the entity to save.
     * @return the persisted entity.
     */
    FormationDTO save(FormationDTO formationDTO);

    /**
     * Updates a formation.
     *
     * @param formationDTO the entity to update.
     * @return the persisted entity.
     */
    FormationDTO update(FormationDTO formationDTO);

    /**
     * Partially updates a formation.
     *
     * @param formationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormationDTO> partialUpdate(FormationDTO formationDTO);

    /**
     * Get all the formations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormationDTO> findAll(Pageable pageable);

    /**
     * Get all the FormationDTO where FormationPrivee is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<FormationDTO> findAllWhereFormationPriveeIsNull();

    /**
     * Get the "id" formation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormationDTO> findOne(Long id);

    /**
     * Delete the "id" formation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the formation corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormationDTO> search(String query, Pageable pageable);
}
