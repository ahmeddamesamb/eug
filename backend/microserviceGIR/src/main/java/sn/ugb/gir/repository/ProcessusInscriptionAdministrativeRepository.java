package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Etudiant;
import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.domain.ProcessusInscriptionAdministrative;

import java.util.Optional;
import java.util.List;

/**
 * Spring Data JPA repository for the ProcessusInscriptionAdministrative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessusInscriptionAdministrativeRepository extends JpaRepository<ProcessusInscriptionAdministrative, Long> {

    List<ProcessusInscriptionAdministrative> findByInscriptionAdministrativeEtudiantCodeEtu(String codeEtudiant);
    Optional<ProcessusInscriptionAdministrative> findByInscriptionAdministrativeEtudiantId(Long etudiantId);



}
