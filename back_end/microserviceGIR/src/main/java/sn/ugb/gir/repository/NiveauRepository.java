package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Niveau;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data JPA repository for the Niveau entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NiveauRepository extends JpaRepository<Niveau, Long> {
    // Requête pour récupérer tous les niveaux par universite
    @Query("SELECT n FROM Niveau n " +
        "JOIN Formation f ON f.niveau.id = n.id " +
        "JOIN Specialite s ON f.specialite.id = s.id " +
        "JOIN Mention m ON s.mention.id = m.id " +
        "JOIN Domaine d ON m.domaine.id = d.id " +
        "JOIN d.ufrs u " + // Relation ManyToMany entre Domaine et Ufr
        "JOIN Universite uni ON u.universite.id = uni.id " +
        "WHERE uni.id = :universiteId")
    Page<Niveau> getAllNiveauByUniversiteId(@Param("universiteId") Long universiteId, Pageable pageable);

    // Requête pour récupérer tous les niveaux par ministère
    @Query("SELECT n FROM Niveau n " +
        "JOIN Formation f ON f.niveau.id = n.id " +
        "JOIN Specialite s ON f.specialite.id = s.id " +
        "JOIN Mention m ON s.mention.id = m.id " +
        "JOIN Domaine d ON m.domaine.id = d.id " +
        "JOIN d.ufrs u " + // Relation ManyToMany entre Domaine et Ufr
        "JOIN Universite uni ON u.universite.id = uni.id " +
        "JOIN Ministere min ON uni.ministere.id = min.id " +
        "WHERE min.id = :ministereId")
    Page<Niveau> getAllNiveauByMinistereId(@Param("ministereId") Long universiteId, Pageable pageable);

    Optional<Niveau> findByAnneeEtude(String anneeEtude);
}
