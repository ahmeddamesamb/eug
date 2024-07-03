package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.CycleDTO;
import sn.ugb.gir.service.dto.FraisDTO;
import sn.ugb.gir.domain.Cycle;

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
     * Get all the frais with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FraisDTO> findAllWithEagerRelationships(Pageable pageable);

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
     * Search for the frais corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FraisDTO> search(String query, Pageable pageable);

    /**
     * Get all the frais by cycle .
     *
     * @param pageable the pagination information.
     * @param cycleID
     * @return the list of entities.
     */
    Page<FraisDTO> findAllFraisByCycleId(Pageable pageable, Long cycleID);

    /**
     * Get all the frais by an id of universite .
     *
     * @param pageable the pagination information.
     * @param universiteId
     * @return the list of entities.
     */
    Page<FraisDTO> findAllFraisByUniversiteId(Pageable pageable, Long universiteId);

    /**
     * Get all the frais by an id of ministere .
     *
     * @param pageable the pagination information.
     * @param ministereId
     * @return the list of entities.
     */
    Page<FraisDTO> findAllFraisByMinistereId(Pageable pageable, Long ministereId);


}
