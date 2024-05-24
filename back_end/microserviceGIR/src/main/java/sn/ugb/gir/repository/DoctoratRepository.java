package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Doctorat;

/**
 * Spring Data JPA repository for the Doctorat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctoratRepository extends JpaRepository<Doctorat, Long> {}
