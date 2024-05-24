package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Baccalaureat;

/**
 * Spring Data JPA repository for the Baccalaureat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BaccalaureatRepository extends JpaRepository<Baccalaureat, Long> {}
