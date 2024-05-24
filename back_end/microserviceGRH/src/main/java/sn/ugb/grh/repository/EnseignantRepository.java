package sn.ugb.grh.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.grh.domain.Enseignant;

/**
 * Spring Data JPA repository for the Enseignant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {}
