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

    Page<Ministere> findByDateDebutBetweenAndDateFinBetween(LocalDate startDate, LocalDate endDate, LocalDate startDate2, LocalDate endDate2, Pageable pageable);

    Page<Ministere> findByDateDebutBetweenAndDateFinBetweenOrDateDebutBetweenAndEnCoursYN(LocalDate startDate, LocalDate endDate, LocalDate startDate2, LocalDate endDate2, LocalDate startDate3, LocalDate endDate3, Integer enCoursYN, Pageable pageable);

    Optional<Ministere> findByNomMinistereIgnoreCase(String nomMinistere);

}
