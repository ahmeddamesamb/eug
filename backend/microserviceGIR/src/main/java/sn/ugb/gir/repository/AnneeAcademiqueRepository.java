package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.AnneeAcademique;

/**
 * Spring Data JPA repository for the AnneeAcademique entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnneeAcademiqueRepository extends JpaRepository<AnneeAcademique, Long> {}
