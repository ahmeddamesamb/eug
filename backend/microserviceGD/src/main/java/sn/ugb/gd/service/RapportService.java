package sn.ugb.gd.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gd.service.dto.RapportDTO;

/**
 * Service Interface for managing {@link sn.ugb.gd.domain.Rapport}.
 */
public interface RapportService {
    /**
     * Save a rapport.
     *
     * @param rapportDTO the entity to save.
     * @return the persisted entity.
     */
    RapportDTO save(RapportDTO rapportDTO);

    /**
     * Updates a rapport.
     *
     * @param rapportDTO the entity to update.
     * @return the persisted entity.
     */
    RapportDTO update(RapportDTO rapportDTO);

    /**
     * Partially updates a rapport.
     *
     * @param rapportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RapportDTO> partialUpdate(RapportDTO rapportDTO);

    /**
     * Get all the rapports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RapportDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rapport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RapportDTO> findOne(Long id);

    /**
     * Delete the "id" rapport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the rapport corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RapportDTO> search(String query, Pageable pageable);
}
