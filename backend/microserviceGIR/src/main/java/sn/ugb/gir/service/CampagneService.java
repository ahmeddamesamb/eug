package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.CampagneDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Campagne}.
 */
public interface CampagneService {
    /**
     * Save a campagne.
     *
     * @param campagneDTO the entity to save.
     * @return the persisted entity.
     */
    CampagneDTO save(CampagneDTO campagneDTO);

    /**
     * Updates a campagne.
     *
     * @param campagneDTO the entity to update.
     * @return the persisted entity.
     */
    CampagneDTO update(CampagneDTO campagneDTO);

    /**
     * Partially updates a campagne.
     *
     * @param campagneDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CampagneDTO> partialUpdate(CampagneDTO campagneDTO);

    /**
     * Get all the campagnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CampagneDTO> findAll(Pageable pageable);

    /**
     * Get the "id" campagne.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CampagneDTO> findOne(Long id);

    /**
     * Delete the "id" campagne.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
