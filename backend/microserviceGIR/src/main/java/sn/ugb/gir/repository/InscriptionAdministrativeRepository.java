package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.InscriptionAdministrative;

/**
 * Spring Data JPA repository for the InscriptionAdministrative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscriptionAdministrativeRepository extends JpaRepository<InscriptionAdministrative, Long> {
    long countByNouveauInscritYNTrue();

    long countByNouveauInscritYNTrueAndAnneeAcademiqueId(Long anneeAcademiqueId);

    long countByNouveauInscritYNTrueAndAnneeAcademiqueAnneeCouranteYNTrue();
}
