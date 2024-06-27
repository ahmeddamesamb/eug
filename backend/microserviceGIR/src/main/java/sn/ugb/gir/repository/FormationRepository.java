package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Formation;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Formation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {

    Page<Formation> findBySpecialiteMentionId(Long mentionId, Pageable pageable);

    Page<Formation> findBySpecialiteMentionDomaineId(Long domaineId, Pageable pageable);

    Page<Formation> findBySpecialiteMentionDomaineUfrsId(Long ufrId, Pageable pageable);

    Page<Formation> findByNiveauTypeCycleLibelleCycleIgnoreCase(String cycle, Pageable pageable);

    Page<Formation> findBySpecialiteMentionDomaineUfrsUniversiteId(Long universiteId, Pageable pageable);

    Page<Formation> findBySpecialiteMentionDomaineUfrsUniversiteMinistereId(Long ministereId, Pageable pageable);

    Page<Formation> findBySpecialiteMentionDomaineUfrsUniversiteIdAndTypeFormationLibelleTypeFormation(Long universiteId, String libelleTypeFormation, Pageable pageable);

    Page<Formation> findBySpecialiteMentionDomaineUfrsUniversiteMinistereIdAndTypeFormationLibelleTypeFormation(Long ministereId, String libelleTypeFormation, Pageable pageable);

    Optional<Formation> findByNiveauIdAndSpecialiteId(Long id, Long id1);
}
