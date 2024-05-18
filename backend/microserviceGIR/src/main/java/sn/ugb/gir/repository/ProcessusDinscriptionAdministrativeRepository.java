package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.ProcessusDinscriptionAdministrative;

/**
 * Spring Data JPA repository for the ProcessusDinscriptionAdministrative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessusDinscriptionAdministrativeRepository extends JpaRepository<ProcessusDinscriptionAdministrative, Long> {}
