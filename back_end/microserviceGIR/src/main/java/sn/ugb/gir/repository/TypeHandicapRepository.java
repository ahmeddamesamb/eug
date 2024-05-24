package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.TypeHandicap;

/**
 * Spring Data JPA repository for the TypeHandicap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeHandicapRepository extends JpaRepository<TypeHandicap, Long> {}
