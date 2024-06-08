package sn.ugb.gir.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Mention;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Mention entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MentionRepository extends JpaRepository<Mention, Long> {
    Page<Mention> findByDomaineUfrsUniversiteMinistereId(Long ministereId, Pageable pageable);

    Page<Mention> findByDomaineUfrsUniversiteId(Long universiteId, Pageable pageable);

    Optional<Mention> findByLibelleMentionIgnoreCase(String libelleMention);

    Page<Mention> findByDomaineUfrsId(Long ufrId, Pageable pageable);
}
