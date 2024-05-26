package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.TypeOperation;

/**
 * Spring Data JPA repository for the TypeOperation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeOperationRepository extends JpaRepository<TypeOperation, Long> {}
