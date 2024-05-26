package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Ministere;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Ministere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MinistereRepository extends JpaRepository<Ministere, Long> {

    Optional<Ministere> findByEnCoursYN(Integer enCours);

    @Query("SELECT m FROM Ministere m WHERE (m.dateDebut >= :startDate AND m.dateDebut <= :endDate) AND (m.dateFin >= :startDate AND m.dateFin <= :endDate)")
    Page<Ministere> findByPeriode(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Optional<Object> findByNomMinistereIgnoreCase(String nomMinistere);

}
