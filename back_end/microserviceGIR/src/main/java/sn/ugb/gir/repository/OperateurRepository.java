package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Operateur;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Operateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperateurRepository extends JpaRepository<Operateur, Long> {
    boolean existsOperateurByCodeOperateur(String codeOperateur);
    boolean existsOperateurByLibelleOperateur(String libelleOperateur);
    Optional <Operateur> findByCodeOperateurIgnoreCase(String codeOperateur);
    Optional <Operateur> findByLibelleOperateurIgnoreCase(String libelleOperateur);

}
