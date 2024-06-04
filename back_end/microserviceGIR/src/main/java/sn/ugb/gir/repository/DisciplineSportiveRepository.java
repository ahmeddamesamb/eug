package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.DisciplineSportive;

import java.util.Optional;

/**
 * Spring Data JPA repository for the DisciplineSportive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisciplineSportiveRepository extends JpaRepository<DisciplineSportive, Long> {
    Optional<DisciplineSportive> findByLibelleDisciplineSportive(String libelleDisciplineSportive);
}
