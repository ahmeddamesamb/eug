package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.UniversiteDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Universite}.
 */
public interface UniversiteService {
    /**
     * Save a universite.
     *
     * @param universiteDTO the entity to save.
     * @return the persisted entity.
     */
    UniversiteDTO save(UniversiteDTO universiteDTO);

    /**
     * Updates a universite.
     *
     * @param universiteDTO the entity to update.
     * @return the persisted entity.
     */
    UniversiteDTO update(UniversiteDTO universiteDTO, Long id);

    /**
     * Partially updates a universite.
     *
     * @param universiteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UniversiteDTO> partialUpdate(UniversiteDTO universiteDTO, Long id);

    /**
     * Get all the universites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UniversiteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" universite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UniversiteDTO> findOne(Long id);

    /**
     * Delete the "id" universite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the universites having ministere id .
     *
     * @param pageable the pagination information.
     * @param id
     * @return the list of entities.
     */
    Page<UniversiteDTO> findAllByMinistereId(Pageable pageable,Long id);
}
