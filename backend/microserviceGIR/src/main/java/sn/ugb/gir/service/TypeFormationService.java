package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.TypeFormationDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.TypeFormation}.
 */
public interface TypeFormationService {
    /**
     * Save a typeFormation.
     *
     * @param typeFormationDTO the entity to save.
     * @return the persisted entity.
     */
    TypeFormationDTO save(TypeFormationDTO typeFormationDTO);

    /**
     * Updates a typeFormation.
     *
     * @param typeFormationDTO the entity to update.
     * @return the persisted entity.
     */
    TypeFormationDTO update(TypeFormationDTO typeFormationDTO);

    /**
     * Partially updates a typeFormation.
     *
     * @param typeFormationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeFormationDTO> partialUpdate(TypeFormationDTO typeFormationDTO);

    /**
     * Get all the typeFormations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeFormationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" typeFormation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeFormationDTO> findOne(Long id);

    /**
     * Delete the "id" typeFormation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the typeFormation corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeFormationDTO> search(String query, Pageable pageable);
}
