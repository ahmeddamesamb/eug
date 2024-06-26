package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.TypeFormation;

/**
 * Spring Data JPA repository for the TypeFormation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeFormationRepository extends JpaRepository<TypeFormation, Long> {}
