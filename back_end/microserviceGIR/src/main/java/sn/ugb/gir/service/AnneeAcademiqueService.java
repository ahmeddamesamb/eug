package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.service.dto.AnneeAcademiqueDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.AnneeAcademique}.
 */
public interface AnneeAcademiqueService {
    /**
     * Save a anneeAcademique.
     *
     * @param anneeAcademiqueDTO the entity to save.
     * @return the persisted entity.
     */
    AnneeAcademiqueDTO save(AnneeAcademiqueDTO anneeAcademiqueDTO);

    /**
     * Updates a anneeAcademique.
     *
     * @param anneeAcademiqueDTO the entity to update.
     * @return the persisted entity.
     */
    AnneeAcademiqueDTO update(AnneeAcademiqueDTO anneeAcademiqueDTO);

    /**
     * Partially updates a anneeAcademique.
     *
     * @param anneeAcademiqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AnneeAcademiqueDTO> partialUpdate(AnneeAcademiqueDTO anneeAcademiqueDTO);

    /**
     * Get all the anneeAcademiques.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnneeAcademiqueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" anneeAcademique.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnneeAcademiqueDTO> findOne(Long id);

    /**
     * Delete the "id" anneeAcademique.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<AnneeAcademiqueDTO> getInfosCurrentAnneeAcademique();

    String generateLibelleAnneeAcademique(String anneeAc);
}
