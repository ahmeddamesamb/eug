package sn.ugb.gir.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.FormationAutorisee;

/**
 * Spring Data JPA repository for the FormationAutorisee entity.
 *
 * When extending this class, extend FormationAutoriseeRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface FormationAutoriseeRepository
    extends FormationAutoriseeRepositoryWithBagRelationships, JpaRepository<FormationAutorisee, Long> {
    default Optional<FormationAutorisee> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<FormationAutorisee> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<FormationAutorisee> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
