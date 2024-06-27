package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Serie;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Serie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByLibelleSerieIgnoreCase(String libelleSerie);

    Optional<Serie> findBySigleSerieIgnoreCase(String sigleSerie);

    Optional<Serie> findByCodeSerieIgnoreCase(String codeSerie);
}
