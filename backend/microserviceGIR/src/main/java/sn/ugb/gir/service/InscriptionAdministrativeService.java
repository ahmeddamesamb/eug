package sn.ugb.gir.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.InscriptionAdministrativeDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.InscriptionAdministrative}.
 */
public interface InscriptionAdministrativeService {
    /**
     * Save a inscriptionAdministrative.
     *
     * @param inscriptionAdministrativeDTO the entity to save.
     * @return the persisted entity.
     */
    InscriptionAdministrativeDTO save(InscriptionAdministrativeDTO inscriptionAdministrativeDTO);

    /**
     * Updates a inscriptionAdministrative.
     *
     * @param inscriptionAdministrativeDTO the entity to update.
     * @return the persisted entity.
     */
    InscriptionAdministrativeDTO update(InscriptionAdministrativeDTO inscriptionAdministrativeDTO);

    /**
     * Partially updates a inscriptionAdministrative.
     *
     * @param inscriptionAdministrativeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InscriptionAdministrativeDTO> partialUpdate(InscriptionAdministrativeDTO inscriptionAdministrativeDTO);

    /**
     * Get all the inscriptionAdministratives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InscriptionAdministrativeDTO> findAll(Pageable pageable);

    /**
     * Get all the InscriptionAdministrativeDTO where ProcessusDinscriptionAdministrative is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<InscriptionAdministrativeDTO> findAllWhereProcessusDinscriptionAdministrativeIsNull();

    /**
     * Get the "id" inscriptionAdministrative.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InscriptionAdministrativeDTO> findOne(Long id);

    /**
     * Delete the "id" inscriptionAdministrative.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
