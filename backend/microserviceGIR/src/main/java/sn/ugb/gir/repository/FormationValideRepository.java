package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.FormationValide;

/**
 * Spring Data JPA repository for the FormationValide entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormationValideRepository extends JpaRepository<FormationValide, Long> {}
