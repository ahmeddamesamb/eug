package sn.ugb.gp.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gp.domain.Enseignement;

/**
 * Spring Data JPA repository for the Enseignement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnseignementRepository extends JpaRepository<Enseignement, Long> {}
