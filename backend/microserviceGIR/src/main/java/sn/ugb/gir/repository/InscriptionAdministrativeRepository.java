package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.InscriptionAdministrative;

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
}

