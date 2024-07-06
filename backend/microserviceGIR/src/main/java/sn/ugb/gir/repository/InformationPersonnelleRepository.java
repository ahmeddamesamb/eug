package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.InformationPersonnelle;

import java.util.Optional;

/**
 * Spring Data JPA repository for the InformationPersonnelle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InformationPersonnelleRepository extends JpaRepository<InformationPersonnelle, Long> {

    Optional<InformationPersonnelle> findByEtudiantCodeEtu(String codeEtudiant);
    Long countByNomEtuAndPrenomEtu(String nomEtu, String prenomEtu);
    boolean existsByNomEtuAndPrenomEtu(String nomEtu, String prenomEtu);
    InformationPersonnelle findByEtudiantId(Long etudiantId);



}
