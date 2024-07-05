package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.domain.PaiementFrais;

import java.util.Optional;

/**
 * Spring Data JPA repository for the InscriptionAdministrativeFormation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscriptionAdministrativeFormationRepository extends JpaRepository<InscriptionAdministrativeFormation, Long> {
    Optional<InscriptionAdministrativeFormation>  findByFormationIdAndInscriptionAdministrativeEtudiantIdAndInscriptionAdministrativeAnneeAcademiqueId(Long idFormation, Long idEtudiant,Long idAnneeAcademique);
    Page<InscriptionAdministrativeFormation> findByInscriptionAdministrativeEtudiantIdAndInscriptionAdministrativeAnneeAcademiqueId(Pageable pageable,Long idEtudiant, Long idAnneeAcademique);
    @Transactional
    @Query("SELECT iaf.id FROM InscriptionAdministrativeFormation iaf WHERE iaf.id = :id")
    InscriptionAdministrativeFormation findByOne(Long id);


}
