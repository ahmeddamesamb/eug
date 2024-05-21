package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;

/**
 * Spring Data JPA repository for the InscriptionAdministrativeFormation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscriptionAdministrativeFormationRepository extends JpaRepository<InscriptionAdministrativeFormation, Long> {}
