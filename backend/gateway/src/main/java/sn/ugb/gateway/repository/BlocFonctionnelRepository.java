package sn.ugb.gateway.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.domain.BlocFonctionnel;

/**
 * Spring Data R2DBC repository for the BlocFonctionnel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlocFonctionnelRepository extends ReactiveCrudRepository<BlocFonctionnel, Long>, BlocFonctionnelRepositoryInternal {
    Flux<BlocFonctionnel> findAllBy(Pageable pageable);

    @Query("SELECT * FROM bloc_fonctionnel entity WHERE entity.service_id = :id")
    Flux<BlocFonctionnel> findByService(Long id);

    @Query("SELECT * FROM bloc_fonctionnel entity WHERE entity.service_id IS NULL")
    Flux<BlocFonctionnel> findAllWhereServiceIsNull();

    @Override
    <S extends BlocFonctionnel> Mono<S> save(S entity);

    @Override
    Flux<BlocFonctionnel> findAll();

    @Override
    Mono<BlocFonctionnel> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BlocFonctionnelRepositoryInternal {
    <S extends BlocFonctionnel> Mono<S> save(S entity);

    Flux<BlocFonctionnel> findAllBy(Pageable pageable);

    Flux<BlocFonctionnel> findAll();

    Mono<BlocFonctionnel> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<BlocFonctionnel> findAllBy(Pageable pageable, Criteria criteria);
}
