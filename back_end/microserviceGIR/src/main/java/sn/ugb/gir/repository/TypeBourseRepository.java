package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.TypeBourse;

/**
 * Spring Data JPA repository for the TypeBourse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeBourseRepository extends JpaRepository<TypeBourse, Long> {}
