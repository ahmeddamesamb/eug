package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Frais;
import sn.ugb.gir.domain.enumeration.Cycle;

/**
 * Spring Data JPA repository for the Frais entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FraisRepository extends JpaRepository<Frais, Long> {
     Page<Frais> findByCycle(Pageable pageable, Cycle cycle);
     boolean existsByCycleAndTypeFraisLibelleTypeFrais(Cycle cycle,String libelleTypeFrais);

    @Modifying
    @Transactional
    @Query("UPDATE Frais f SET f.estEnApplicationYN = 0 WHERE f.estEnApplicationYN = 1 and f.cycle= :cycle and f.fraisPourAssimileYN = :fraisPourAssimileYN")
    void updateIfEstEnApplicationIsOneAndCycleAndFraisPourAssimileYNLike(Cycle cycle, Integer fraisPourAssimileYN );


}
