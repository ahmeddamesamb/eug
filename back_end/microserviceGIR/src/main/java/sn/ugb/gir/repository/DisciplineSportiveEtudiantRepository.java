package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.DisciplineSportiveEtudiant;

/**
 * Spring Data JPA repository for the DisciplineSportiveEtudiant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisciplineSportiveEtudiantRepository extends JpaRepository<DisciplineSportiveEtudiant, Long> {}
