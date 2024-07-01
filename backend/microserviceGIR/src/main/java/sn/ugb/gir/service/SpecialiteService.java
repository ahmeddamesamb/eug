package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.SpecialiteDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Specialite}.
 */
public interface SpecialiteService {
    /**
     * Save a specialite.
     *
     * @param specialiteDTO the entity to save.
     * @return the persisted entity.
     */
    SpecialiteDTO save(SpecialiteDTO specialiteDTO);

    /**
     * Updates a specialite.
     *
     * @param specialiteDTO the entity to update.
     * @return the persisted entity.
     */
    SpecialiteDTO update(SpecialiteDTO specialiteDTO);

    /**
     * Partially updates a specialite.
     *
     * @param specialiteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpecialiteDTO> partialUpdate(SpecialiteDTO specialiteDTO);

    /**
     * Get all the specialites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SpecialiteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" specialite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpecialiteDTO> findOne(Long id);

    /**
     * Delete the "id" specialite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the specialite corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SpecialiteDTO> search(String query, Pageable pageable);

    Page<SpecialiteDTO> getAllSpecialiteByMention(Long mentionId, Pageable pageable);

    Page<SpecialiteDTO> getAllSpecialiteByDomaine(Long domaineId, Pageable pageable);

    Page<SpecialiteDTO> getAllSpecialiteByUfr(Long ufrId, Pageable pageable);

    Page<SpecialiteDTO> getAllSpecialiteByUniversite(Long universiteId, Pageable pageable);

    Page<SpecialiteDTO> getAllSpecialiteByMinistere(Long ministereId, Pageable pageable);

    SpecialiteDTO setActifYNSpecialite(Long id, Boolean actifYN);
}
