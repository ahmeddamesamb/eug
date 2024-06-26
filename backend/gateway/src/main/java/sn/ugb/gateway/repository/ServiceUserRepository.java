package sn.ugb.gateway.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.domain.ServiceUser;

/**
 * Spring Data R2DBC repository for the ServiceUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceUserRepository extends ReactiveCrudRepository<ServiceUser, Long>, ServiceUserRepositoryInternal {
    Flux<ServiceUser> findAllBy(Pageable pageable);

    @Override
    <S extends ServiceUser> Mono<S> save(S entity);

    @Override
    Flux<ServiceUser> findAll();

    @Override
    Mono<ServiceUser> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ServiceUserRepositoryInternal {
    <S extends ServiceUser> Mono<S> save(S entity);

    Flux<ServiceUser> findAllBy(Pageable pageable);

    Flux<ServiceUser> findAll();

    Mono<ServiceUser> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ServiceUser> findAllBy(Pageable pageable, Criteria criteria);
}
