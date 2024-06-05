package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Specialite;
import sn.ugb.gir.domain.TypeBourse;

import java.util.Optional;

/**
 * Spring Data JPA repository for the TypeBourse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeBourseRepository extends JpaRepository<TypeBourse, Long> {

    //static Optional<TypeBourse> findByLibelleTypeBourseIgnoreCase(String nomSpecialites);

    Optional<TypeBourse> findByLibelleTypeBourse(String libelleTypeBourse);

    Optional<TypeBourse> findByLibelleTypeBourseAndIdNot(String libelleTypeBourse, Long id);

    Optional<TypeBourse> findByLibelleTypeBourseIgnoreCase(String trim);
}
