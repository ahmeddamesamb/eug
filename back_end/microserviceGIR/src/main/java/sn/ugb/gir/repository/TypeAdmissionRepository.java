package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.TypeAdmission;

import java.util.Optional;

/**
 * Spring Data JPA repository for the TypeAdmission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeAdmissionRepository extends JpaRepository<TypeAdmission, Long> {
    Optional<TypeAdmission> findByLibelleTypeAdmission(String libelleTypeAdmission);
}
