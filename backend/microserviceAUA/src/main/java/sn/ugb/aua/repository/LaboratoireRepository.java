package sn.ugb.aua.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.aua.domain.Laboratoire;

/**
 * Spring Data JPA repository for the Laboratoire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LaboratoireRepository extends JpaRepository<Laboratoire, Long> {}
