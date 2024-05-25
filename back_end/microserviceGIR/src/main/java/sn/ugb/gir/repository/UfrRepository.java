package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Ufr;

/**
 * Spring Data JPA repository for the Ufr entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UfrRepository extends JpaRepository<Ufr, Long> {
    Page<Ufr> findByUniversiteId(Long universiteId, Pageable pageable);

    Page<Ufr> findByUniversiteMinistereId(Long mentionId, Pageable pageable);
}
