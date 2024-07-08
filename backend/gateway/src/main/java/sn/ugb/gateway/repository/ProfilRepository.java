package sn.ugb.gateway.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.domain.Profil;

/**
 * Spring Data R2DBC repository for the Profil entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfilRepository extends ReactiveCrudRepository<Profil, Long>, ProfilRepositoryInternal {
    Flux<Profil> findAllBy(Pageable pageable);

    @Override
    <S extends Profil> Mono<S> save(S entity);

    @Override
    Flux<Profil> findAll();

    @Override
    Mono<Profil> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
    Flux<Profil> findByActifYN(Boolean actifYN,Pageable pageable);
}

interface ProfilRepositoryInternal {
    <S extends Profil> Mono<S> save(S entity);

    Flux<Profil> findAllBy(Pageable pageable);

    Flux<Profil> findAll();

    Mono<Profil> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Profil> findAllBy(Pageable pageable, Criteria criteria);
}
