package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.domain.PaiementFrais;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the InscriptionAdministrativeFormation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscriptionAdministrativeFormationRepository extends JpaRepository<InscriptionAdministrativeFormation, Long> {
    Optional<InscriptionAdministrativeFormation>  findByFormationIdAndInscriptionAdministrativeEtudiantIdAndInscriptionAdministrativeAnneeAcademiqueId(Long idFormation, Long idEtudiant,Long idAnneeAcademique);

    List<InscriptionAdministrativeFormation> findByInscriptionAdministrativeEtudiantIdAndInscriptionAdministrativeAnneeAcademiqueId( Long idEtudiant, Long idAnneeAcademique);

    @Transactional
    @Query("SELECT iaf.id FROM InscriptionAdministrativeFormation iaf WHERE iaf.id = :id")
    InscriptionAdministrativeFormation findByOne(Long id);

    @Query( "SELECT iaf FROM InscriptionAdministrativeFormation iaf  WHERE ( iaf.inscriptionAdministrative.anneeAcademique.anneeAc) " +
        " = (SELECT MAX(i.inscriptionAdministrative.anneeAcademique.anneeAc) FROM InscriptionAdministrativeFormation i " +
        " WHERE (iaf.inscriptionAdministrative.etudiant = i.inscriptionAdministrative.etudiant) GROUP BY i.inscriptionAdministrative.etudiant) ")
    Page<InscriptionAdministrativeFormation> findByLastInscription(Pageable pageable);


//    @Query( "SELECT iaf FROM InscriptionAdministrativeFormation iaf  WHERE (iaf.inscriptionAdministrative.anneeAcademique.anneeAc) >=ALL (" SELECT i.inscriptionAdministrative.anneeAcademique.anneeAc FROM InscriptionAdministrativeFormation i WHERE( i.inscriptionAdministrative.etudiant.id == iaf.inscriptionAdministrative.etudiant.id )")")
//    Page<InscriptionAdministrativeFormation> teste(Pageable pageable);




}
