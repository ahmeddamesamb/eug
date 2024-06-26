package sn.ugb.gd.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gd.domain.TypeRapport;

/**
 * Spring Data JPA repository for the TypeRapport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeRapportRepository extends JpaRepository<TypeRapport, Long> {}
