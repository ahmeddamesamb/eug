package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.DoctoratDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Doctorat}.
 */
public interface DoctoratService {
    /**
     * Save a doctorat.
     *
     * @param doctoratDTO the entity to save.
     * @return the persisted entity.
     */
    DoctoratDTO save(DoctoratDTO doctoratDTO);

    /**
     * Updates a doctorat.
     *
     * @param doctoratDTO the entity to update.
     * @return the persisted entity.
     */
    DoctoratDTO update(DoctoratDTO doctoratDTO);

    /**
     * Partially updates a doctorat.
     *
     * @param doctoratDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DoctoratDTO> partialUpdate(DoctoratDTO doctoratDTO);

    /**
     * Get all the doctorats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DoctoratDTO> findAll(Pageable pageable);

    /**
     * Get the "id" doctorat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DoctoratDTO> findOne(Long id);

    /**
     * Delete the "id" doctorat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the doctorat corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DoctoratDTO> search(String query, Pageable pageable);
}
