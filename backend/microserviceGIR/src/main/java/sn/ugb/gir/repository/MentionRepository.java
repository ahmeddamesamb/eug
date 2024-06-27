package sn.ugb.gir.repository;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Mention;
import sn.ugb.gir.service.dto.MentionDTO;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Mention entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MentionRepository extends JpaRepository<Mention, Long> {
    Optional<Mention> findByLibelleMentionIgnoreCase(String libelleMention);

    Page<MentionDTO> findByDomaineUfrsUniversiteMinistereId(Long ministereId, Pageable pageable);

    Page<MentionDTO> findByDomaineUfrsUniversiteId(Long universiteId, Pageable pageable);

    Page<MentionDTO> findByDomaineUfrsId(Long ufrId, Pageable pageable);
}
