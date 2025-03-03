package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.PaysDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Pays}.
 */
public interface PaysService {
    /**
     * Save a pays.
     *
     * @param paysDTO the entity to save.
     * @return the persisted entity.
     */
    PaysDTO save(PaysDTO paysDTO);

    /**
     * Updates a pays.
     *
     * @param paysDTO the entity to update.
     * @return the persisted entity.
     */
    PaysDTO update(PaysDTO paysDTO);

    /**
     * Partially updates a pays.
     *
     * @param paysDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaysDTO> partialUpdate(PaysDTO paysDTO);

    /**
     * Get all the pays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaysDTO> findAll(Pageable pageable);

    /**
     * Get all the pays with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaysDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" pays.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaysDTO> findOne(Long id);

    /**
     * Delete the "id" pays.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the pays corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaysDTO> search(String query, Pageable pageable);
    Page<PaysDTO> findAllPaysByZone(Long zoneId, Pageable pageable);
}
