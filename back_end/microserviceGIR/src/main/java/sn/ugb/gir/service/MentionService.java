package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.MentionDTO;
import sn.ugb.gir.service.dto.MentionDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.Mention}.
 */
public interface MentionService {
    /**
     * Save a mention.
     *
     * @param mentionDTO the entity to save.
     * @return the persisted entity.
     */
    MentionDTO save(MentionDTO mentionDTO);

    /**
     * Updates a mention.
     *
     * @param mentionDTO the entity to update.
     * @return the persisted entity.
     */
    MentionDTO update(MentionDTO mentionDTO);

    /**
     * Partially updates a mention.
     *
     * @param mentionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MentionDTO> partialUpdate(MentionDTO mentionDTO);

    /**
     * Get all the mentions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MentionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mention.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MentionDTO> findOne(Long id);

    /**
     * Delete the "id" mention.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<MentionDTO> getAllMentionByUfr(Long ufrId, Pageable pageable);

    Page<MentionDTO> getAllMentionByUniversite(Long universiteId, Pageable pageable);

    Page<MentionDTO> getAllMentionByMinistere(Long ministereId, Pageable pageable);
}
