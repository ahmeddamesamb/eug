package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Frais;
import sn.ugb.gir.domain.enumeration.Cycle;

/**
 * Spring Data JPA repository for the Frais entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FraisRepository extends JpaRepository<Frais, Long> {
     Page<Frais> findFraisByCycle(Pageable pageable, Cycle cycle);
     boolean existsFraisByCycleAndTypeFraisLibelleTypeFrais(Cycle cycle,String typeFrais);

}
