package sn.ugb.aua.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.aua.service.dto.LaboratoireDTO;

/**
 * Service Interface for managing {@link sn.ugb.aua.domain.Laboratoire}.
 */
public interface LaboratoireService {
    /**
     * Save a laboratoire.
     *
     * @param laboratoireDTO the entity to save.
     * @return the persisted entity.
     */
    LaboratoireDTO save(LaboratoireDTO laboratoireDTO);

    /**
     * Updates a laboratoire.
     *
     * @param laboratoireDTO the entity to update.
     * @return the persisted entity.
     */
    LaboratoireDTO update(LaboratoireDTO laboratoireDTO);

    /**
     * Partially updates a laboratoire.
     *
     * @param laboratoireDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LaboratoireDTO> partialUpdate(LaboratoireDTO laboratoireDTO);

    /**
     * Get all the laboratoires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LaboratoireDTO> findAll(Pageable pageable);

    /**
     * Get the "id" laboratoire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LaboratoireDTO> findOne(Long id);

    /**
     * Delete the "id" laboratoire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
