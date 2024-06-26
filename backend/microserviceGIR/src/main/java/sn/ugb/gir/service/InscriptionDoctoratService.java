package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.InscriptionDoctoratDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.InscriptionDoctorat}.
 */
public interface InscriptionDoctoratService {
    /**
     * Save a inscriptionDoctorat.
     *
     * @param inscriptionDoctoratDTO the entity to save.
     * @return the persisted entity.
     */
    InscriptionDoctoratDTO save(InscriptionDoctoratDTO inscriptionDoctoratDTO);

    /**
     * Updates a inscriptionDoctorat.
     *
     * @param inscriptionDoctoratDTO the entity to update.
     * @return the persisted entity.
     */
    InscriptionDoctoratDTO update(InscriptionDoctoratDTO inscriptionDoctoratDTO);

    /**
     * Partially updates a inscriptionDoctorat.
     *
     * @param inscriptionDoctoratDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InscriptionDoctoratDTO> partialUpdate(InscriptionDoctoratDTO inscriptionDoctoratDTO);

    /**
     * Get all the inscriptionDoctorats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InscriptionDoctoratDTO> findAll(Pageable pageable);

    /**
     * Get the "id" inscriptionDoctorat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InscriptionDoctoratDTO> findOne(Long id);

    /**
     * Delete the "id" inscriptionDoctorat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the inscriptionDoctorat corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InscriptionDoctoratDTO> search(String query, Pageable pageable);
}
