package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.DisciplineSportiveDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.DisciplineSportive}.
 */
public interface DisciplineSportiveService {
    /**
     * Save a disciplineSportive.
     *
     * @param disciplineSportiveDTO the entity to save.
     * @return the persisted entity.
     */
    DisciplineSportiveDTO save(DisciplineSportiveDTO disciplineSportiveDTO);

    /**
     * Updates a disciplineSportive.
     *
     * @param disciplineSportiveDTO the entity to update.
     * @return the persisted entity.
     */
    DisciplineSportiveDTO update(DisciplineSportiveDTO disciplineSportiveDTO);

    /**
     * Partially updates a disciplineSportive.
     *
     * @param disciplineSportiveDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DisciplineSportiveDTO> partialUpdate(DisciplineSportiveDTO disciplineSportiveDTO);

    /**
     * Get all the disciplineSportives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DisciplineSportiveDTO> findAll(Pageable pageable);

    /**
     * Get the "id" disciplineSportive.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DisciplineSportiveDTO> findOne(Long id);

    /**
     * Delete the "id" disciplineSportive.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
