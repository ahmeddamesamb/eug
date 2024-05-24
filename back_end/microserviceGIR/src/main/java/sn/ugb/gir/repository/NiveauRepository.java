package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Niveau;

/**
 * Spring Data JPA repository for the Niveau entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NiveauRepository extends JpaRepository<Niveau, Long> {}
