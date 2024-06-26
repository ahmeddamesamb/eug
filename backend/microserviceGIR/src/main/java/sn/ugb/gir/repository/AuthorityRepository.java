package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.ugb.gir.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
