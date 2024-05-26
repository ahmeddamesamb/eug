package sn.ugb.ged.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.ugb.ged.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
