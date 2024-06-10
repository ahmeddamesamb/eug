package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.DisciplineSportiveEtudiant;
import sn.ugb.gir.domain.Lycee;

import java.util.Optional;

/**
 * Spring Data JPA repository for the DisciplineSportiveEtudiant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisciplineSportiveEtudiantRepository extends JpaRepository<DisciplineSportiveEtudiant, Long> {

    Page<DisciplineSportiveEtudiant> findAllByEtudiantCodeEtu(Pageable pageable, String codeEtu);
    Page<DisciplineSportiveEtudiant> findAllByEtudiantId(Pageable pageable,Long id);
    Page<DisciplineSportiveEtudiant> findAllByDisciplineSportiveId(Pageable pageable,Long id);
    Optional<DisciplineSportiveEtudiant> findByDisciplineSportiveIdAndEtudiantId(Long idDS, Long idEtu);
}
