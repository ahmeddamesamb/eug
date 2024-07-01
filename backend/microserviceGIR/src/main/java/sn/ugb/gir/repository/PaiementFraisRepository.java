package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.PaiementFrais;

import java.util.List;

/**
 * Spring Data JPA repository for the PaiementFrais entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaiementFraisRepository extends JpaRepository<PaiementFrais, Long> {
    List<PaiementFrais> findByInscriptionAdministrativeFormationInscriptionAdministrativeEtudiantCodeEtu(String codeEtudiant);
}
