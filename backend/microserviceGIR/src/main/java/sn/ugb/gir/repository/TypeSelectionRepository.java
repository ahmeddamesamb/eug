package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.TypeSelection;

/**
 * Spring Data JPA repository for the TypeSelection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeSelectionRepository extends JpaRepository<TypeSelection, Long> {}
