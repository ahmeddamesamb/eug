package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.InscriptionAdministrative;
import sn.ugb.gir.service.dto.InformationsDerniersInscriptionsDTO;
import sn.ugb.gir.service.dto.InformationsIADTO;

import java.util.Optional;

/**
 * Spring Data JPA repository for the InscriptionAdministrative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscriptionAdministrativeRepository extends JpaRepository<InscriptionAdministrative, Long> {
    long countByNouveauInscritYNTrue();

    long countByNouveauInscritYNTrueAndAnneeAcademiqueId(Long anneeAcademiqueId);

    long countByNouveauInscritYNTrueAndAnneeAcademiqueAnneeCouranteYNTrue();

    Page<InscriptionAdministrative> findByEtudiantIdAndAnneeAcademiqueId(Pageable pageable,Long etudiantId, Long AnneeAcademiqueId);

    @Query("SELECT new sn.ugb.gir.service.dto.InformationsIADTO(ia,ip) " +
        "FROM InscriptionAdministrative ia, InformationPersonnelle ip " +
        "WHERE ia.anneeAcademique.anneeAc = " +
        "(SELECT MAX(i.inscriptionAdministrative.anneeAcademique.anneeAc) " +
        "FROM InscriptionAdministrativeFormation i " +
        "WHERE ia.etudiant = i.inscriptionAdministrative.etudiant " +
        "AND ip.etudiant.id = ia.etudiant.id " +
        "GROUP BY i.inscriptionAdministrative.etudiant)")
    Page<InformationsIADTO> findByInscriptionEnCours(Pageable pageable);
}

