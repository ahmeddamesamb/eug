package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.InscriptionDoctorat;

/**
 * Spring Data JPA repository for the InscriptionDoctorat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscriptionDoctoratRepository extends JpaRepository<InscriptionDoctorat, Long> {}
