package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Ministere;

/**
 * Spring Data JPA repository for the Ministere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MinistereRepository extends JpaRepository<Ministere, Long> {}
