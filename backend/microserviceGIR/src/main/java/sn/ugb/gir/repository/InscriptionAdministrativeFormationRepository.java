package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;

import java.util.Optional;

/**
 * Spring Data JPA repository for the InscriptionAdministrativeFormation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscriptionAdministrativeFormationRepository extends JpaRepository<InscriptionAdministrativeFormation, Long> {
    Optional<InscriptionAdministrativeFormation>  findByFormationIdAndInscriptionAdministrativeEtudiantIdAndInscriptionAdministrativeAnneeAcademiqueId(Long idFormation, Long idEtudiant,Long idAnneeAcademique);
    Page<InscriptionAdministrativeFormation> findByInscriptionAdministrativeEtudiantIdAndInscriptionAdministrativeAnneeAcademiqueId(Long idEtudiant, Long idAnneeAcademique);
}
