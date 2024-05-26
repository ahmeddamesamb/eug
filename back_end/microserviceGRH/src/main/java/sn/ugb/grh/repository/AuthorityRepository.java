package sn.ugb.grh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.ugb.grh.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
