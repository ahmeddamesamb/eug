package sn.ugb.gir.repository;

import com.fasterxml.jackson.databind.introspect.AnnotationCollector;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.w3c.dom.html.HTMLOptionElement;
import sn.ugb.gir.domain.AnneeAcademique;
import sn.ugb.gir.domain.Etudiant;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Etudiant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    Optional<Etudiant> findByEmailUGB(String emailUGB);

    Optional<Etudiant> findByCodeEtu(String codeEtu);

    boolean existsByCodeEtuIgnoreCase(String codeEtu);

    boolean existsByEmailUGBIgnoreCase(String emailUGB);

    boolean existsByNumDocIdentiteIgnoreCase(String numDocIdentite);

    Etudiant findByCodeBU(String codeBU);

    @Query("SELECT MAX(e.codeBU) FROM Etudiant e")
    String findMaxCodeBU();

}
