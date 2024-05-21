package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.UFRDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.UFR}.
 */
public interface UFRService {
    /**
     * Save a uFR.
     *
     * @param uFRDTO the entity to save.
     * @return the persisted entity.
     */
    UFRDTO save(UFRDTO uFRDTO);

    /**
     * Updates a uFR.
     *
     * @param uFRDTO the entity to update.
     * @return the persisted entity.
     */
    UFRDTO update(UFRDTO uFRDTO);

    /**
     * Partially updates a uFR.
     *
     * @param uFRDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UFRDTO> partialUpdate(UFRDTO uFRDTO);

    /**
     * Get all the uFRS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UFRDTO> findAll(Pageable pageable);

    /**
     * Get all the uFRS with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UFRDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" uFR.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UFRDTO> findOne(Long id);

    /**
     * Delete the "id" uFR.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
