package sn.ugb.gateway.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.domain.InfoUserRessource;

/**
 * Spring Data R2DBC repository for the InfoUserRessource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfoUserRessourceRepository extends ReactiveCrudRepository<InfoUserRessource, Long>, InfoUserRessourceRepositoryInternal {
    Flux<InfoUserRessource> findAllBy(Pageable pageable);

    @Query("SELECT * FROM info_user_ressource entity WHERE entity.infos_user_id = :id")
    Flux<InfoUserRessource> findByInfosUser(Long id);

    @Query("SELECT * FROM info_user_ressource entity WHERE entity.infos_user_id IS NULL")
    Flux<InfoUserRessource> findAllWhereInfosUserIsNull();

    @Query("SELECT * FROM info_user_ressource entity WHERE entity.ressource_id = :id")
    Flux<InfoUserRessource> findByRessource(Long id);

    @Query("SELECT * FROM info_user_ressource entity WHERE entity.ressource_id IS NULL")
    Flux<InfoUserRessource> findAllWhereRessourceIsNull();

    @Override
    <S extends InfoUserRessource> Mono<S> save(S entity);

    @Override
    Flux<InfoUserRessource> findAll();

    @Override
    Mono<InfoUserRessource> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface InfoUserRessourceRepositoryInternal {
    <S extends InfoUserRessource> Mono<S> save(S entity);

    Flux<InfoUserRessource> findAllBy(Pageable pageable);

    Flux<InfoUserRessource> findAll();

    Mono<InfoUserRessource> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<InfoUserRessource> findAllBy(Pageable pageable, Criteria criteria);
}
