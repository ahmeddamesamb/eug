package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Specialite;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Specialite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {
    Page<Specialite> findByMentionId(Long mentionId, Pageable pageable);

    Page<Specialite> findByMentionDomaineId(Long ufrId, Pageable pageable);

    Page<Specialite> findByMentionDomaineUfrsId(Long ufrId, Pageable pageable);

    Page<Specialite> findByMentionDomaineUfrsUniversiteId(Long universiteId, Pageable pageable);

    Page<Specialite> findByMentionDomaineUfrsUniversiteMinistereId(Long ministereId, Pageable pageable);

    Optional<Specialite> findByNomSpecialites(String nomSpecialites);

    Optional<Specialite> findBySigleSpecialites(String sigleSpecialites);
}
