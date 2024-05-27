package sn.ugb.gir.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.Pays;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Pays entity.
 * <p>
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
//-------------------------------------------- FIND ALL PAYS BY ZONE ID ------------------------------------------------

Page<Pays> findByZonesId(Long zoneId, Pageable pageable);


}
