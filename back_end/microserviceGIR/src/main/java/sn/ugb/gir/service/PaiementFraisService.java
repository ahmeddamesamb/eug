package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.PaiementFraisDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.PaiementFrais}.
 */
public interface PaiementFraisService {
    /**
     * Save a paiementFrais.
     *
     * @param paiementFraisDTO the entity to save.
     * @return the persisted entity.
     */
    PaiementFraisDTO save(PaiementFraisDTO paiementFraisDTO);

    /**
     * Updates a paiementFrais.
     *
     * @param paiementFraisDTO the entity to update.
     * @return the persisted entity.
     */
    PaiementFraisDTO update(PaiementFraisDTO paiementFraisDTO);

    /**
     * Partially updates a paiementFrais.
     *
     * @param paiementFraisDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaiementFraisDTO> partialUpdate(PaiementFraisDTO paiementFraisDTO);

    /**
     * Get all the paiementFrais.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaiementFraisDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paiementFrais.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaiementFraisDTO> findOne(Long id);

    /**
     * Delete the "id" paiementFrais.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
