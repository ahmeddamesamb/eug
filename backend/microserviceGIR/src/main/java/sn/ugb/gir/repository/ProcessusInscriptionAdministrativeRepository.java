package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.ProcessusInscriptionAdministrative;

/**
 * Spring Data JPA repository for the ProcessusInscriptionAdministrative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessusInscriptionAdministrativeRepository extends JpaRepository<ProcessusInscriptionAdministrative, Long> {}
