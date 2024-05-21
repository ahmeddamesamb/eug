package sn.ugb.deliberation.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.deliberation.service.dto.DeliberationDTO;

/**
 * Service Interface for managing {@link sn.ugb.deliberation.domain.Deliberation}.
 */
public interface DeliberationService {
    /**
     * Save a deliberation.
     *
     * @param deliberationDTO the entity to save.
     * @return the persisted entity.
     */
    DeliberationDTO save(DeliberationDTO deliberationDTO);

    /**
     * Updates a deliberation.
     *
     * @param deliberationDTO the entity to update.
     * @return the persisted entity.
     */
    DeliberationDTO update(DeliberationDTO deliberationDTO);

    /**
     * Partially updates a deliberation.
     *
     * @param deliberationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DeliberationDTO> partialUpdate(DeliberationDTO deliberationDTO);

    /**
     * Get all the deliberations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeliberationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" deliberation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeliberationDTO> findOne(Long id);

    /**
     * Delete the "id" deliberation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
