package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Ufr;

/**
 * Spring Data JPA repository for the Ufr entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UfrRepository extends JpaRepository<Ufr, Long> {}
