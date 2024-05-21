package sn.ugb.gir.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.ugb.gir.domain.UFR;

/**
 * Spring Data JPA repository for the UFR entity.
 */
@Repository
public interface UFRRepository extends JpaRepository<UFR, Long> {
    default Optional<UFR> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UFR> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UFR> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select uFR from UFR uFR left join fetch uFR.universite", countQuery = "select count(uFR) from UFR uFR")
    Page<UFR> findAllWithToOneRelationships(Pageable pageable);

    @Query("select uFR from UFR uFR left join fetch uFR.universite")
    List<UFR> findAllWithToOneRelationships();

    @Query("select uFR from UFR uFR left join fetch uFR.universite where uFR.id =:id")
    Optional<UFR> findOneWithToOneRelationships(@Param("id") Long id);
}
