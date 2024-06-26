package sn.ugb.ged.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.ged.domain.TypeDocument;

/**
 * Spring Data JPA repository for the TypeDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeDocumentRepository extends JpaRepository<TypeDocument, Long> {}
