package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.ProcessusInscriptionAdministrativeDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.ProcessusInscriptionAdministrative}.
 */
public interface ProcessusInscriptionAdministrativeService {
    /**
     * Save a processusInscriptionAdministrative.
     *
     * @param processusInscriptionAdministrativeDTO the entity to save.
     * @return the persisted entity.
     */
    ProcessusInscriptionAdministrativeDTO save(ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO);

    /**
     * Updates a processusInscriptionAdministrative.
     *
     * @param processusInscriptionAdministrativeDTO the entity to update.
     * @return the persisted entity.
     */
    ProcessusInscriptionAdministrativeDTO update(ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO);

    /**
     * Partially updates a processusInscriptionAdministrative.
     *
     * @param processusInscriptionAdministrativeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProcessusInscriptionAdministrativeDTO> partialUpdate(
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO
    );

    /**
     * Get all the processusInscriptionAdministratives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProcessusInscriptionAdministrativeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" processusInscriptionAdministrative.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProcessusInscriptionAdministrativeDTO> findOne(Long id);

    /**
     * Delete the "id" processusInscriptionAdministrative.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the processusInscriptionAdministrative corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProcessusInscriptionAdministrativeDTO> search(String query, Pageable pageable);
}
