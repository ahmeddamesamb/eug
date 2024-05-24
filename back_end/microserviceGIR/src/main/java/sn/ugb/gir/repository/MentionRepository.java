package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Mention;

/**
 * Spring Data JPA repository for the Mention entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MentionRepository extends JpaRepository<Mention, Long> {}
