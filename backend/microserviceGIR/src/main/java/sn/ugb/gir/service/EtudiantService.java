package sn.ugb.gir.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.EtudiantDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Etudiant}.
 */
public interface EtudiantService {
    /**
     * Save a etudiant.
     *
     * @param etudiantDTO the entity to save.
     * @return the persisted entity.
     */
    EtudiantDTO save(EtudiantDTO etudiantDTO);

    /**
     * Updates a etudiant.
     *
     * @param etudiantDTO the entity to update.
     * @return the persisted entity.
     */
    EtudiantDTO update(EtudiantDTO etudiantDTO);

    /**
     * Partially updates a etudiant.
     *
     * @param etudiantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EtudiantDTO> partialUpdate(EtudiantDTO etudiantDTO);

    /**
     * Get all the etudiants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EtudiantDTO> findAll(Pageable pageable);

    /**
     * Get all the EtudiantDTO where InformationPersonnelle is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<EtudiantDTO> findAllWhereInformationPersonnelleIsNull();
    /**
     * Get all the EtudiantDTO where Baccalaureat is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<EtudiantDTO> findAllWhereBaccalaureatIsNull();

    /**
     * Get the "id" etudiant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EtudiantDTO> findOne(Long id);

    /**
     * Delete the "id" etudiant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the etudiant corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EtudiantDTO> search(String query, Pageable pageable);
}
