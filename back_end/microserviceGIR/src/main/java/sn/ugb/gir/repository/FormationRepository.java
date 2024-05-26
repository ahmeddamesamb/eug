package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.enumeration.Cycle;
import sn.ugb.gir.domain.enumeration.TypeFormation;
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

    Page<Formation> findByNiveauCycleNiveau(Cycle cycle, Pageable pageable);

    Page<Formation> findBySpecialiteMentionDomaineUfrsUniversiteId(Long universiteId, Pageable pageable);

    Page<Formation> findBySpecialiteMentionDomaineUfrsUniversiteMinistereId(Long ministereId, Pageable pageable);

    Page<Formation> findBySpecialiteMentionDomaineUfrsUniversiteIdAndTypeFormation(Long universiteId, TypeFormation typeFormation, Pageable pageable);

    Page<Formation> findBySpecialiteMentionDomaineUfrsUniversiteMinistereIdAndTypeFormation(Long ministereId, TypeFormation typeFormation, Pageable pageable);

    Optional<Formation> findByNiveauIdAndSpecialiteId(Long id, Long id1);

}
