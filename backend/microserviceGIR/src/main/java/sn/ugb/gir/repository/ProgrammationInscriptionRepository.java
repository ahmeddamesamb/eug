package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.ProgrammationInscription;

/**
 * Spring Data JPA repository for the ProgrammationInscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProgrammationInscriptionRepository extends JpaRepository<ProgrammationInscription, Long> {}
