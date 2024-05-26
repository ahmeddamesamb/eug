package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.ProgrammationInscriptionDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.ProgrammationInscription}.
 */
public interface ProgrammationInscriptionService {
    /**
     * Save a programmationInscription.
     *
     * @param programmationInscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    ProgrammationInscriptionDTO save(ProgrammationInscriptionDTO programmationInscriptionDTO);

    /**
     * Updates a programmationInscription.
     *
     * @param programmationInscriptionDTO the entity to update.
     * @return the persisted entity.
     */
    ProgrammationInscriptionDTO update(ProgrammationInscriptionDTO programmationInscriptionDTO);

    /**
     * Partially updates a programmationInscription.
     *
     * @param programmationInscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProgrammationInscriptionDTO> partialUpdate(ProgrammationInscriptionDTO programmationInscriptionDTO);

    /**
     * Get all the programmationInscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProgrammationInscriptionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" programmationInscription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProgrammationInscriptionDTO> findOne(Long id);

    /**
     * Delete the "id" programmationInscription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
