package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.AnneeAcademique;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.FormationInvalide;

import java.util.Optional;

/**
 * Spring Data JPA repository for the FormationInvalide entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormationInvalideRepository extends JpaRepository<FormationInvalide, Long> {
    Optional<FormationInvalide> findByFormationIdAndAnneeAcademiqueId(Long formationId, Long anneeAcademiqueId);
}
