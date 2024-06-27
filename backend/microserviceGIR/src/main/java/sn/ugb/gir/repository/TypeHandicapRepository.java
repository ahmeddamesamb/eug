package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.TypeHandicap;

import java.util.Optional;

/**
 * Spring Data JPA repository for the TypeHandicap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeHandicapRepository extends JpaRepository<TypeHandicap, Long> {
    Optional<TypeHandicap> findByLibelleTypeHandicapIgnoreCase(String trim);
}
