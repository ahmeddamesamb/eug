package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.PaiementFormationPrivee;

/**
 * Spring Data JPA repository for the PaiementFormationPrivee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaiementFormationPriveeRepository extends JpaRepository<PaiementFormationPrivee, Long> {}
