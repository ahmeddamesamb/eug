package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.DomaineDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Domaine}.
 */
public interface DomaineService {
    /**
     * Save a domaine.
     *
     * @param domaineDTO the entity to save.
     * @return the persisted entity.
     */
    DomaineDTO save(DomaineDTO domaineDTO);

    /**
     * Updates a domaine.
     *
     * @param domaineDTO the entity to update.
     * @return the persisted entity.
     */
    DomaineDTO update(DomaineDTO domaineDTO);

    /**
     * Partially updates a domaine.
     *
     * @param domaineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DomaineDTO> partialUpdate(DomaineDTO domaineDTO);

    /**
     * Get all the domaines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DomaineDTO> findAll(Pageable pageable);

    /**
     * Get all the domaines with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DomaineDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" domaine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DomaineDTO> findOne(Long id);

    /**
     * Delete the "id" domaine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
Page<DomaineDTO> findAllDomaineByUfr(Long ufrId, Pageable pageable);

Page<DomaineDTO> findAllDomaineByUniversite(Long universiteId, Pageable pageable);

Page<DomaineDTO> findAllDomaineByMinistere(Long ministereId, Pageable pageable);
}
