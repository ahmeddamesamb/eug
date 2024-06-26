package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.InformationImageDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.InformationImage}.
 */
public interface InformationImageService {
    /**
     * Save a informationImage.
     *
     * @param informationImageDTO the entity to save.
     * @return the persisted entity.
     */
    InformationImageDTO save(InformationImageDTO informationImageDTO);

    /**
     * Updates a informationImage.
     *
     * @param informationImageDTO the entity to update.
     * @return the persisted entity.
     */
    InformationImageDTO update(InformationImageDTO informationImageDTO);

    /**
     * Partially updates a informationImage.
     *
     * @param informationImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InformationImageDTO> partialUpdate(InformationImageDTO informationImageDTO);

    /**
     * Get all the informationImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InformationImageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" informationImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InformationImageDTO> findOne(Long id);

    /**
     * Delete the "id" informationImage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the informationImage corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InformationImageDTO> search(String query, Pageable pageable);
}
