package sn.ugb.aide.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.aide.domain.Ressource;

/**
 * Spring Data JPA repository for the Ressource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RessourceRepository extends JpaRepository<Ressource, Long> {}
