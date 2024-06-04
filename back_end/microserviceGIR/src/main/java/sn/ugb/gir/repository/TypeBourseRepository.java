package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.TypeBourse;

import java.util.Optional;

/**
 * Spring Data JPA repository for the TypeBourse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeBourseRepository extends JpaRepository<TypeBourse, Long> {
    Optional<TypeBourse> findByLibelleTypeBourse(String libelleTypeBourse);
}
