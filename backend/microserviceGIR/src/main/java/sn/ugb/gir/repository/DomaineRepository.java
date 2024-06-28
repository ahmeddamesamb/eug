package sn.ugb.gir.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Domaine;

/**
 * Spring Data JPA repository for the Domaine entity.
 *
 * When extending this class, extend DomaineRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface DomaineRepository extends DomaineRepositoryWithBagRelationships, JpaRepository<Domaine, Long> {
    default Optional<Domaine> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Domaine> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Domaine> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
    Page<Domaine> findByUfrsId(Long ufrId, Pageable pageable);

    Page<Domaine> findByUfrsUniversiteId(Long universiteId, Pageable pageable);

    Page<Domaine> findByUfrsUniversiteMinistereId(Long ministereId, Pageable pageable);

    Optional<Domaine> findByLibelleDomaineIgnoreCase(String libelleDomaine);
}
