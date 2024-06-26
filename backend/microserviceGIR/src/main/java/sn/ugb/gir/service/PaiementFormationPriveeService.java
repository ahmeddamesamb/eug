package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.PaiementFormationPriveeDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.PaiementFormationPrivee}.
 */
public interface PaiementFormationPriveeService {
    /**
     * Save a paiementFormationPrivee.
     *
     * @param paiementFormationPriveeDTO the entity to save.
     * @return the persisted entity.
     */
    PaiementFormationPriveeDTO save(PaiementFormationPriveeDTO paiementFormationPriveeDTO);

    /**
     * Updates a paiementFormationPrivee.
     *
     * @param paiementFormationPriveeDTO the entity to update.
     * @return the persisted entity.
     */
    PaiementFormationPriveeDTO update(PaiementFormationPriveeDTO paiementFormationPriveeDTO);

    /**
     * Partially updates a paiementFormationPrivee.
     *
     * @param paiementFormationPriveeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaiementFormationPriveeDTO> partialUpdate(PaiementFormationPriveeDTO paiementFormationPriveeDTO);

    /**
     * Get all the paiementFormationPrivees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaiementFormationPriveeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paiementFormationPrivee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaiementFormationPriveeDTO> findOne(Long id);

    /**
     * Delete the "id" paiementFormationPrivee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the paiementFormationPrivee corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaiementFormationPriveeDTO> search(String query, Pageable pageable);
}
