package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Domaine;
import sn.ugb.gir.domain.Region;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Region entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

//-------------------------------------------- FIND ALL REGIONS BY PAYS ------------------------------------------------
Page<Region> findByPaysId(Long paysId, Pageable pageable);
//-------------------------------------------- FIND BY  LIBELLEREGION ------------------------------------------------
Optional<Region> findByLibelleRegion(String libelleRegion);
}
