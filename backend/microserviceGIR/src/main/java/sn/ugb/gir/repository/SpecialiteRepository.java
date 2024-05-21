package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Specialite;

/**
 * Spring Data JPA repository for the Specialite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {}
