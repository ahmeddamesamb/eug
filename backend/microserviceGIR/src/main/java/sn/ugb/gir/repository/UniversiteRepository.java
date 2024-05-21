package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Universite;

/**
 * Spring Data JPA repository for the Universite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniversiteRepository extends JpaRepository<Universite, Long> {}
