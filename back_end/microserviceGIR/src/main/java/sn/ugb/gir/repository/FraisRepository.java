package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Frais;

/**
 * Spring Data JPA repository for the Frais entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FraisRepository extends JpaRepository<Frais, Long> {}
