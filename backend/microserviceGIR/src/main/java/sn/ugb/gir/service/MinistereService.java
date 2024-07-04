package sn.ugb.gir.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.MinistereDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Ministere}.
 */
public interface MinistereService {
    /**
     * Save a ministere.
     *
     * @param ministereDTO the entity to save.
     * @return the persisted entity.
     */
    MinistereDTO save(MinistereDTO ministereDTO);

    /**
     * Updates a ministere.
     *
     * @param ministereDTO the entity to update.
     * @return the persisted entity.
     */
    MinistereDTO update(MinistereDTO ministereDTO);

    /**
     * Partially updates a ministere.
     *
     * @param ministereDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MinistereDTO> partialUpdate(MinistereDTO ministereDTO);

    /**
     * Get all the ministeres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MinistereDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ministere.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MinistereDTO> findOne(Long id);

    /**
     * Delete the "id" ministere.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the ministere corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MinistereDTO> search(String query, Pageable pageable);

    Optional<MinistereDTO> findCurrent();

    Page<MinistereDTO> findByPeriode(LocalDate startDate, LocalDate endDate, Pageable pageable);


    MinistereDTO setActifYNMinistere(Long id, Boolean actifYN);
}
