package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.OperateurDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Operateur}.
 */
public interface OperateurService {
    /**
     * Save a operateur.
     *
     * @param operateurDTO the entity to save.
     * @return the persisted entity.
     */
    OperateurDTO save(OperateurDTO operateurDTO);

    /**
     * Updates a operateur.
     *
     * @param operateurDTO the entity to update.
     * @return the persisted entity.
     */
    OperateurDTO update(OperateurDTO operateurDTO);

    /**
     * Partially updates a operateur.
     *
     * @param operateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OperateurDTO> partialUpdate(OperateurDTO operateurDTO);

    /**
     * Get all the operateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OperateurDTO> findAll(Pageable pageable);

    /**
     * Get the "id" operateur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OperateurDTO> findOne(Long id);

    /**
     * Delete the "id" operateur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
