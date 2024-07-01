package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.InformationPersonnelle;

/**
 * Spring Data JPA repository for the InformationPersonnelle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InformationPersonnelleRepository extends JpaRepository<InformationPersonnelle, Long> {
    InformationPersonnelle findByEtudiantCodeEtu(String codeEtudiant);
}
