package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.FormationPrivee;

/**
 * Spring Data JPA repository for the FormationPrivee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormationPriveeRepository extends JpaRepository<FormationPrivee, Long> {}
