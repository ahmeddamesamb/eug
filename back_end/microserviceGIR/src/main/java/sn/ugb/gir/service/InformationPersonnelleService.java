package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.InformationPersonnelleDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.InformationPersonnelle}.
 */
public interface InformationPersonnelleService {
    /**
     * Save a informationPersonnelle.
     *
     * @param informationPersonnelleDTO the entity to save.
     * @return the persisted entity.
     */
    InformationPersonnelleDTO save(InformationPersonnelleDTO informationPersonnelleDTO);

    /**
     * Updates a informationPersonnelle.
     *
     * @param informationPersonnelleDTO the entity to update.
     * @return the persisted entity.
     */
    InformationPersonnelleDTO update(InformationPersonnelleDTO informationPersonnelleDTO);

    /**
     * Partially updates a informationPersonnelle.
     *
     * @param informationPersonnelleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InformationPersonnelleDTO> partialUpdate(InformationPersonnelleDTO informationPersonnelleDTO);

    /**
     * Get all the informationPersonnelles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InformationPersonnelleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" informationPersonnelle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InformationPersonnelleDTO> findOne(Long id);

    /**
     * Delete the "id" informationPersonnelle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
