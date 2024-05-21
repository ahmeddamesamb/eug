package sn.ugb.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.ugb.user.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
