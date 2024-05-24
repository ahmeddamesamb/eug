package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.UfrDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Ufr}.
 */
public interface UfrService {
    /**
     * Save a ufr.
     *
     * @param ufrDTO the entity to save.
     * @return the persisted entity.
     */
    UfrDTO save(UfrDTO ufrDTO);

    /**
     * Updates a ufr.
     *
     * @param ufrDTO the entity to update.
     * @return the persisted entity.
     */
    UfrDTO update(UfrDTO ufrDTO);

    /**
     * Partially updates a ufr.
     *
     * @param ufrDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UfrDTO> partialUpdate(UfrDTO ufrDTO);

    /**
     * Get all the ufrs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UfrDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ufr.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UfrDTO> findOne(Long id);

    /**
     * Delete the "id" ufr.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
