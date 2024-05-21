package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.BaccalaureatDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Baccalaureat}.
 */
public interface BaccalaureatService {
    /**
     * Save a baccalaureat.
     *
     * @param baccalaureatDTO the entity to save.
     * @return the persisted entity.
     */
    BaccalaureatDTO save(BaccalaureatDTO baccalaureatDTO);

    /**
     * Updates a baccalaureat.
     *
     * @param baccalaureatDTO the entity to update.
     * @return the persisted entity.
     */
    BaccalaureatDTO update(BaccalaureatDTO baccalaureatDTO);

    /**
     * Partially updates a baccalaureat.
     *
     * @param baccalaureatDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BaccalaureatDTO> partialUpdate(BaccalaureatDTO baccalaureatDTO);

    /**
     * Get all the baccalaureats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BaccalaureatDTO> findAll(Pageable pageable);

    /**
     * Get the "id" baccalaureat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BaccalaureatDTO> findOne(Long id);

    /**
     * Delete the "id" baccalaureat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
