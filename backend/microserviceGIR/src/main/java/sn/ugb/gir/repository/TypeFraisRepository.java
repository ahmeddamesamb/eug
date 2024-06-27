package sn.ugb.gir.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.TypeFrais;

import java.util.Optional;

/**
 * Spring Data JPA repository for the TypeFrais entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeFraisRepository extends JpaRepository<TypeFrais, Long> {
    Optional<TypeFrais> findByLibelleTypeFraisIgnoreCase(String trim);
}
