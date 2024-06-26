package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Lycee;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Lycee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LyceeRepository extends JpaRepository<Lycee, Long> {
    boolean existsLyceeByNomLycee(String nonLycee);
    Page<Lycee> findAllByRegionId(Pageable pageable, Long id);
    Optional <Lycee> findByNomLyceeIgnoreCase(String nomLycee);

}
