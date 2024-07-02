package sn.ugb.gateway.repository;

import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.domain.InfosUser;

/**
 * Spring Data R2DBC repository for the InfosUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfosUserRepository extends ReactiveCrudRepository<InfosUser, Long>, InfosUserRepositoryInternal {
    Flux<InfosUser> findAllBy(Pageable pageable);

    @Override
    Mono<InfosUser> findOneWithEagerRelationships(Long id);

    @Override
    Flux<InfosUser> findAllWithEagerRelationships();

    @Override
    Flux<InfosUser> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM infos_user entity WHERE entity.user_id = :id")
    Flux<InfosUser> findByUser(Long id);

    @Query("SELECT * FROM infos_user entity WHERE entity.user_id IS NULL")
    Flux<InfosUser> findAllWhereUserIsNull();

    @Override
    <S extends InfosUser> Mono<S> save(S entity);

    @Override
    Flux<InfosUser> findAll();

    @Override
    Mono<InfosUser> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);

}

interface InfosUserRepositoryInternal {
    <S extends InfosUser> Mono<S> save(S entity);

    Flux<InfosUser> findAllBy(Pageable pageable);

    Flux<InfosUser> findAll();

    Mono<InfosUser> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<InfosUser> findAllBy(Pageable pageable, Criteria criteria);

    Mono<InfosUser> findOneWithEagerRelationships(Long id);

    Flux<InfosUser> findAllWithEagerRelationships();

    Flux<InfosUser> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
