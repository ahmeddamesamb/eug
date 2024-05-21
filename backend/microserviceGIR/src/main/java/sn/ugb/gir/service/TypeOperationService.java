package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.TypeOperationDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.TypeOperation}.
 */
public interface TypeOperationService {
    /**
     * Save a typeOperation.
     *
     * @param typeOperationDTO the entity to save.
     * @return the persisted entity.
     */
    TypeOperationDTO save(TypeOperationDTO typeOperationDTO);

    /**
     * Updates a typeOperation.
     *
     * @param typeOperationDTO the entity to update.
     * @return the persisted entity.
     */
    TypeOperationDTO update(TypeOperationDTO typeOperationDTO);

    /**
     * Partially updates a typeOperation.
     *
     * @param typeOperationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeOperationDTO> partialUpdate(TypeOperationDTO typeOperationDTO);

    /**
     * Get all the typeOperations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeOperationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" typeOperation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeOperationDTO> findOne(Long id);

    /**
     * Delete the "id" typeOperation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
