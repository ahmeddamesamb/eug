package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.SerieDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Serie}.
 */
public interface SerieService {
    /**
     * Save a serie.
     *
     * @param serieDTO the entity to save.
     * @return the persisted entity.
     */
    SerieDTO save(SerieDTO serieDTO);

    /**
     * Updates a serie.
     *
     * @param serieDTO the entity to update.
     * @return the persisted entity.
     */
    SerieDTO update(SerieDTO serieDTO);

    /**
     * Partially updates a serie.
     *
     * @param serieDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SerieDTO> partialUpdate(SerieDTO serieDTO);

    /**
     * Get all the series.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SerieDTO> findAll(Pageable pageable);

    /**
     * Get the "id" serie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SerieDTO> findOne(Long id);

    /**
     * Delete the "id" serie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the serie corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SerieDTO> search(String query, Pageable pageable);
}
