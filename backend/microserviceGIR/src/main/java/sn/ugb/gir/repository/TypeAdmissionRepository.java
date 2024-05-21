package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.TypeAdmission;

/**
 * Spring Data JPA repository for the TypeAdmission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeAdmissionRepository extends JpaRepository<TypeAdmission, Long> {}
