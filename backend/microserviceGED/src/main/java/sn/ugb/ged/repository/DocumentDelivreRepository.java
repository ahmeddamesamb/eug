package sn.ugb.ged.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.ged.domain.DocumentDelivre;

/**
 * Spring Data JPA repository for the DocumentDelivre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentDelivreRepository extends JpaRepository<DocumentDelivre, Long> {}
