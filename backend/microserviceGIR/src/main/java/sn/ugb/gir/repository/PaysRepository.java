package sn.ugb.gir.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Pays;

/**
 * Spring Data JPA repository for the Pays entity.
 *
 * When extending this class, extend PaysRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PaysRepository extends PaysRepositoryWithBagRelationships, JpaRepository<Pays, Long> {
    default Optional<Pays> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Pays> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Pays> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
    Page<Pays> findByZonesId(Long zoneId, Pageable pageable);
    Optional<Pays> findByLibellePaysIgnoreCase(String libellePays);
}
