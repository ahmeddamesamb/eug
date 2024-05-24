package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.ProcessusDinscriptionAdministrativeDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.ProcessusDinscriptionAdministrative}.
 */
public interface ProcessusDinscriptionAdministrativeService {
    /**
     * Save a processusDinscriptionAdministrative.
     *
     * @param processusDinscriptionAdministrativeDTO the entity to save.
     * @return the persisted entity.
     */
    ProcessusDinscriptionAdministrativeDTO save(ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO);

    /**
     * Updates a processusDinscriptionAdministrative.
     *
     * @param processusDinscriptionAdministrativeDTO the entity to update.
     * @return the persisted entity.
     */
    ProcessusDinscriptionAdministrativeDTO update(ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO);

    /**
     * Partially updates a processusDinscriptionAdministrative.
     *
     * @param processusDinscriptionAdministrativeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProcessusDinscriptionAdministrativeDTO> partialUpdate(
        ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO
    );

    /**
     * Get all the processusDinscriptionAdministratives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProcessusDinscriptionAdministrativeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" processusDinscriptionAdministrative.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProcessusDinscriptionAdministrativeDTO> findOne(Long id);

    /**
     * Delete the "id" processusDinscriptionAdministrative.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
