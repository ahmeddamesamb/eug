package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.DisciplineSportive;

/**
 * Spring Data JPA repository for the DisciplineSportive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisciplineSportiveRepository extends JpaRepository<DisciplineSportive, Long> {}
