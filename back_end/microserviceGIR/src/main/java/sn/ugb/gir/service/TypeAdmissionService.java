package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.TypeAdmissionDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.TypeAdmission}.
 */
public interface TypeAdmissionService {
    /**
     * Save a typeAdmission.
     *
     * @param typeAdmissionDTO the entity to save.
     * @return the persisted entity.
     */
    TypeAdmissionDTO save(TypeAdmissionDTO typeAdmissionDTO);

    /**
     * Updates a typeAdmission.
     *
     * @param typeAdmissionDTO the entity to update.
     * @return the persisted entity.
     */
    TypeAdmissionDTO update(TypeAdmissionDTO typeAdmissionDTO);

    /**
     * Partially updates a typeAdmission.
     *
     * @param typeAdmissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeAdmissionDTO> partialUpdate(TypeAdmissionDTO typeAdmissionDTO);

    /**
     * Get all the typeAdmissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeAdmissionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" typeAdmission.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeAdmissionDTO> findOne(Long id);

    /**
     * Delete the "id" typeAdmission.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
