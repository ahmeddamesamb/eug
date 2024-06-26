package sn.ugb.aide.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.aide.domain.RessourceAide;

/**
 * Spring Data JPA repository for the RessourceAide entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RessourceAideRepository extends JpaRepository<RessourceAide, Long> {}
