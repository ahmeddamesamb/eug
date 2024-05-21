package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.FormationValideDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.FormationValide}.
 */
public interface FormationValideService {
    /**
     * Save a formationValide.
     *
     * @param formationValideDTO the entity to save.
     * @return the persisted entity.
     */
    FormationValideDTO save(FormationValideDTO formationValideDTO);

    /**
     * Updates a formationValide.
     *
     * @param formationValideDTO the entity to update.
     * @return the persisted entity.
     */
    FormationValideDTO update(FormationValideDTO formationValideDTO);

    /**
     * Partially updates a formationValide.
     *
     * @param formationValideDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormationValideDTO> partialUpdate(FormationValideDTO formationValideDTO);

    /**
     * Get all the formationValides.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormationValideDTO> findAll(Pageable pageable);

    /**
     * Get the "id" formationValide.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormationValideDTO> findOne(Long id);

    /**
     * Delete the "id" formationValide.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
