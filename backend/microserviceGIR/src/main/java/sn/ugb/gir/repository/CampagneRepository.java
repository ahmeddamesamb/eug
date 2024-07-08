package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Campagne;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Campagne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CampagneRepository extends JpaRepository<Campagne, Long> {
    Optional<Campagne> findByDateDebutAndDateFin(LocalDate dateDebut, LocalDate dateFin);
}
