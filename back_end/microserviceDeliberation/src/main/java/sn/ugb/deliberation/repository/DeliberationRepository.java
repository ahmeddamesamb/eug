package sn.ugb.deliberation.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.deliberation.domain.Deliberation;

/**
 * Spring Data JPA repository for the Deliberation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliberationRepository extends JpaRepository<Deliberation, Long> {}
