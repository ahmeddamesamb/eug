package sn.ugb.gir.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ugb.gir.service.dto.InformationsDerniersInscriptionsDTO;
import sn.ugb.gir.service.dto.InscriptionAdministrativeFormationDTO;

/**
 * Service Interface for managing {@link sn.ugb.gir.domain.InscriptionAdministrativeFormation}.
 */
public interface InscriptionAdministrativeFormationService {
    /**
     * Save a inscriptionAdministrativeFormation.
     *
     * @param inscriptionAdministrativeFormationDTO the entity to save.
     * @return the persisted entity.
     */
    InscriptionAdministrativeFormationDTO save(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO);

    /**
     * Updates a inscriptionAdministrativeFormation.
     *
     * @param inscriptionAdministrativeFormationDTO the entity to update.
     * @return the persisted entity.
     */
    InscriptionAdministrativeFormationDTO update(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO);

    /**
     * Partially updates a inscriptionAdministrativeFormation.
     *
     * @param inscriptionAdministrativeFormationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InscriptionAdministrativeFormationDTO> partialUpdate(
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO
    );

    /**
     * Get all the inscriptionAdministrativeFormations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InscriptionAdministrativeFormationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" inscriptionAdministrativeFormation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InscriptionAdministrativeFormationDTO> findOne(Long id);

    /**
     * Delete the "id" inscriptionAdministrativeFormation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the inscriptionAdministrativeFormation corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InscriptionAdministrativeFormationDTO> search(String query, Pageable pageable);

    /**
     * Get all the last inscriptionAdministrativeFormations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InformationsDerniersInscriptionsDTO> findAllByDernierInscription(Pageable pageable);

//    Page<Object[]> findAllByDernierInscription(Pageable pageable);

}
