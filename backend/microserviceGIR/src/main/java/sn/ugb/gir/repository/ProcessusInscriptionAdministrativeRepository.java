package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Etudiant;
import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.domain.ProcessusInscriptionAdministrative;

<<<<<<< HEAD
import java.util.Optional;
=======
import java.util.List;
>>>>>>> ff337b31e885f5a19fd4d125ce9809f53e6985f9

/**
 * Spring Data JPA repository for the ProcessusInscriptionAdministrative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessusInscriptionAdministrativeRepository extends JpaRepository<ProcessusInscriptionAdministrative, Long> {
<<<<<<< HEAD

=======
    List<ProcessusInscriptionAdministrative> findByInscriptionAdministrativeEtudiantCodeEtu(String codeEtudiant);
>>>>>>> ff337b31e885f5a19fd4d125ce9809f53e6985f9
}
