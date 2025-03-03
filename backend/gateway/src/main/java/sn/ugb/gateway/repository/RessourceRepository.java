package sn.ugb.gateway.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.domain.Ressource;

/**
 * Spring Data R2DBC repository for the Ressource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RessourceRepository extends ReactiveCrudRepository<Ressource, Long>, RessourceRepositoryInternal {
    Flux<Ressource> findAllBy(Pageable pageable);

    @Override
    <S extends Ressource> Mono<S> save(S entity);

    @Override
    Flux<Ressource> findAll();

    @Override
    Mono<Ressource> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);


    //Flux<Ressource> findByServiceId(Long serviceId, Pageable pageable);
    //
    //Flux<Ressource> findByBlocFonctionnelId(Long blocfonctionnelId, Pageable pageable);

    //@Query("SELECT COUNT(*) FROM ressource WHERE service_id = :serviceId")
    //Mono<Long> countByServiceId(Long serviceId);
    //
    //@Query("SELECT COUNT(*) FROM ressource WHERE bloc_fonctionnel_id = :blocfonctionnelId")
    //Mono<Long> countByBlocFonctionnelId(Long blocfonctionnelId);
}

interface RessourceRepositoryInternal {
    Flux<Ressource> findAllByProfileId(Long userProfileId, Pageable pageable);

    Flux<Ressource> findAllByInfoUserId(Long infoUserId, Pageable pageable);

    <S extends Ressource> Mono<S> save(S entity);

    Flux<Ressource> findAllBy(Pageable pageable);

    Flux<Ressource> findAll();

    Mono<Ressource> findById(Long id);


    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Ressource> findAllBy(Pageable pageable, Criteria criteria);


    /*Flux<Ressource> findAllByInfoUserId(Long infoUserId, Pageable pageable);


    Mono<Long> countByInfoUserId(Long infoUserId);


    Mono<Long> countByUserProfileId(Long userProfileId);

    Flux<Ressource> findAllByUserProfileId(Long userProfileId, Pageable pageable);
    */

}
