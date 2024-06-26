package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.FormationInvalide;

/**
 * Spring Data JPA repository for the FormationInvalide entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormationInvalideRepository extends JpaRepository<FormationInvalide, Long> {}
