package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.domain.DisciplineSportiveEtudiant;
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
    DisciplineSportiveEtudiantDTO update(DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO, Long id);

    /**
     * Partially updates a disciplineSportiveEtudiant.
     *
     * @param disciplineSportiveEtudiantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DisciplineSportiveEtudiantDTO> partialUpdate(DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO, Long id);

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
     * Get all the disciplineSportive for an Etudiants by codeEtu.
     *
     * @param pageable the pagination information.
     * @param codeEtu the CodeEtu of entity etudiant
     * @return the list of entities.
     */
    Page<DisciplineSportiveEtudiantDTO> findAllByEtudiantCodeEtu(Pageable pageable, String codeEtu);
    Page<DisciplineSportiveEtudiantDTO> findAllByEtudiantId(Pageable pageable,Long id);
    Page<DisciplineSportiveEtudiantDTO> findAllByDisciplineSportiveId(Pageable pageable,Long id);
}
