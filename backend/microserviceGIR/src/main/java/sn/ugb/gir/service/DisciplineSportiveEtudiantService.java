package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.DisciplineSportiveEtudiantDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.DisciplineSportiveEtudiant}.
 */
public interface DisciplineSportiveEtudiantService {
    /**
     * Save a disciplineSportiveEtudiant.
     *
     * @param disciplineSportiveEtudiantDTO the entity to save.
     * @return the persisted entity.
     */
    DisciplineSportiveEtudiantDTO save(DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO);

    /**
     * Updates a disciplineSportiveEtudiant.
     *
     * @param disciplineSportiveEtudiantDTO the entity to update.
     * @return the persisted entity.
     */
    DisciplineSportiveEtudiantDTO update(DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO);

    /**
     * Partially updates a disciplineSportiveEtudiant.
     *
     * @param disciplineSportiveEtudiantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DisciplineSportiveEtudiantDTO> partialUpdate(DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO);

    /**
     * Get all the disciplineSportiveEtudiants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DisciplineSportiveEtudiantDTO> findAll(Pageable pageable);

    /**
     * Get the "id" disciplineSportiveEtudiant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DisciplineSportiveEtudiantDTO> findOne(Long id);

    /**
     * Delete the "id" disciplineSportiveEtudiant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the disciplineSportiveEtudiant corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DisciplineSportiveEtudiantDTO> search(String query, Pageable pageable);
}
