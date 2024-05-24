package sn.ugb.user.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.user.service.dto.UtilisateurDTO;

/**
 * Service Interface for managing {@link sn.ugb.user.domain.Utilisateur}.
 */
public interface UtilisateurService {
    /**
     * Save a utilisateur.
     *
     * @param utilisateurDTO the entity to save.
     * @return the persisted entity.
     */
    UtilisateurDTO save(UtilisateurDTO utilisateurDTO);

    /**
     * Updates a utilisateur.
     *
     * @param utilisateurDTO the entity to update.
     * @return the persisted entity.
     */
    UtilisateurDTO update(UtilisateurDTO utilisateurDTO);

    /**
     * Partially updates a utilisateur.
     *
     * @param utilisateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UtilisateurDTO> partialUpdate(UtilisateurDTO utilisateurDTO);

    /**
     * Get all the utilisateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UtilisateurDTO> findAll(Pageable pageable);

    /**
     * Get the "id" utilisateur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UtilisateurDTO> findOne(Long id);

    /**
     * Delete the "id" utilisateur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
