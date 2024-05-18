package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Domaine;

/**
 * Spring Data JPA repository for the Domaine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DomaineRepository extends JpaRepository<Domaine, Long> {}
