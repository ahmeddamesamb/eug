package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Lycee;

/**
 * Spring Data JPA repository for the Lycee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LyceeRepository extends JpaRepository<Lycee, Long> {}
