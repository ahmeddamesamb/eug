package sn.ugb.aclc.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.aclc.domain.Candidat;

/**
 * Spring Data JPA repository for the Candidat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long> {}
