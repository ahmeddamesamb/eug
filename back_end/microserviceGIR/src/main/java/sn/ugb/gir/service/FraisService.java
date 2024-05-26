package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.domain.enumeration.Cycle;
import sn.ugb.gir.service.dto.FraisDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Frais}.
 */
public interface FraisService {
    /**
     * Save a frais.
     *
     * @param fraisDTO the entity to save.
     * @return the persisted entity.
     */
    FraisDTO save(FraisDTO fraisDTO);

    /**
     * Updates a frais.
     *
     * @param fraisDTO the entity to update.
     * @return the persisted entity.
     */
    FraisDTO update(FraisDTO fraisDTO);

    /**
     * Partially updates a frais.
     *
     * @param fraisDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FraisDTO> partialUpdate(FraisDTO fraisDTO);

    /**
     * Get all the frais.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FraisDTO> findAll(Pageable pageable);

    /**
     * Get the "id" frais.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FraisDTO> findOne(Long id);

    /**
     * Delete the "id" frais.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the frais by cycle .
     *
     * @param pageable the pagination information.
     * @param cycle
     * @return the list of entities.
     */
    Page<FraisDTO> findAllFraisByCycle(Pageable pageable, Cycle cycle);
}
