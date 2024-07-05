package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.domain.PaiementFrais;

import java.util.List;

/**
 * Spring Data JPA repository for the PaiementFrais entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaiementFraisRepository extends JpaRepository<PaiementFrais, Long> {
    List<PaiementFrais> findByInscriptionAdministrativeFormationInscriptionAdministrativeEtudiantCodeEtu(String codeEtudiant);
//    Optional<PaiementFrais> findByInscriptionAdministrativeFormation

    @Transactional
    @Query("SELECT pf FROM PaiementFrais pf WHERE pf.inscriptionAdministrativeFormation.id = :id")
    PaiementFrais paiementFraisIAF(Long id );

}
