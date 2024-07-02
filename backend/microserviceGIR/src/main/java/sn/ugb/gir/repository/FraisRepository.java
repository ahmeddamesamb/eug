package sn.ugb.gir.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Frais;
import sn.ugb.gir.domain.Cycle;

/**
 * Spring Data JPA repository for the Frais entity.
 *
 * When extending this class, extend FraisRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface FraisRepository extends FraisRepositoryWithBagRelationships, JpaRepository<Frais, Long> {
    default Optional<Frais> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Frais> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Frais> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
    Page<Frais> findByTypeCycleId(Pageable pageable, Long cycleID);
    boolean existsByTypeCycleAndTypeFraisLibelleTypeFrais(Cycle cycle,String libelleTypeFrais);

    @Modifying
    @Transactional
    @Query("UPDATE Frais f SET f.estEnApplicationYN = false WHERE f.estEnApplicationYN = true and f.typeCycle= :cycle and f.fraisPourAssimileYN = :fraisPourAssimileYN")
    void updateIfEstEnApplicationIsOneAndCycleAndFraisPourAssimileYNLike(Cycle cycle, Boolean fraisPourAssimileYN );

    Page <Frais> findAllByUniversitesId(Pageable pageable, Long universiteId);
    Page <Frais> findAllByUniversitesMinistereId(Pageable pageable, Long ministereId);


}
