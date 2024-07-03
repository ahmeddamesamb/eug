package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.ProgrammationInscription;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Spring Data JPA repository for the ProgrammationInscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgrammationInscriptionRepository extends JpaRepository<ProgrammationInscription, Long> {
    Optional<ProgrammationInscription> findByFormation_IdAndCampagne_IdAndAnneeAcademique_IdAndDateDebutProgrammationAndDateFinProgrammation(
        Long formationId, Long campagneId, Long anneeAcademiqueId, LocalDate dateDebutProgrammation, LocalDate dateFinProgrammation);
}
