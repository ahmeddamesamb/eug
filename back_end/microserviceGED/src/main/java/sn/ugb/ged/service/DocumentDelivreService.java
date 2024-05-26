package sn.ugb.ged.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.ged.service.dto.DocumentDelivreDTO;

/**
 * Service Interface for managing {@link sn.ugb.ged.domain.DocumentDelivre}.
 */
public interface DocumentDelivreService {
    /**
     * Save a documentDelivre.
     *
     * @param documentDelivreDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentDelivreDTO save(DocumentDelivreDTO documentDelivreDTO);

    /**
     * Updates a documentDelivre.
     *
     * @param documentDelivreDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentDelivreDTO update(DocumentDelivreDTO documentDelivreDTO);

    /**
     * Partially updates a documentDelivre.
     *
     * @param documentDelivreDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentDelivreDTO> partialUpdate(DocumentDelivreDTO documentDelivreDTO);

    /**
     * Get all the documentDelivres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentDelivreDTO> findAll(Pageable pageable);

    /**
     * Get the "id" documentDelivre.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentDelivreDTO> findOne(Long id);

    /**
     * Delete the "id" documentDelivre.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
