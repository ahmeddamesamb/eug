package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Cycle;

/**
 * Spring Data JPA repository for the Cycle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CycleRepository extends JpaRepository<Cycle, Long> {}
