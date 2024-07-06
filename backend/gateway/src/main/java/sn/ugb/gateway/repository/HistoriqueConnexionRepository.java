package sn.ugb.gateway.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.domain.HistoriqueConnexion;

import java.time.LocalDate;

/**
 * Spring Data R2DBC repository for the HistoriqueConnexion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriqueConnexionRepository
    extends ReactiveCrudRepository<HistoriqueConnexion, Long>, HistoriqueConnexionRepositoryInternal {
    Flux<HistoriqueConnexion> findAllBy(Pageable pageable);

    @Query("SELECT * FROM historique_connexion entity WHERE entity.info_user_id = :id")
    Flux<HistoriqueConnexion> findByInfoUser(Long id);

    @Query("SELECT * FROM historique_connexion entity WHERE entity.info_user_id IS NULL")
    Flux<HistoriqueConnexion> findAllWhereInfoUserIsNull();

    @Override
    <S extends HistoriqueConnexion> Mono<S> save(S entity);

    @Override
    Flux<HistoriqueConnexion> findAll();

    @Override
    Mono<HistoriqueConnexion> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);

    @Query("SELECT * FROM historique_connexion entity WHERE entity.actif_yn = :enCoursYN")
    Flux<HistoriqueConnexion> findAllByEnCoursYN(Boolean enCoursYN, Pageable pageable);

    @Query("SELECT * FROM historique_connexion entity WHERE (entity.date_debut_connexion BETWEEN :dateDebut AND :dateFin) AND (entity.date_fin_connexion BETWEEN :dateDebut AND :dateFin)")
    Flux<HistoriqueConnexion> findAllByTimeSlots(LocalDate dateDebut, LocalDate dateFin, Pageable pageable);

    @Query("SELECT * FROM historique_connexion entity WHERE entity.info_user_id = :infosUserId AND (entity.date_debut_connexion BETWEEN :dateDebut AND :dateFin) AND (entity.date_fin_connexion BETWEEN :dateDebut AND :dateFin)")
    Flux<HistoriqueConnexion> findAllByInfosUserIdAndTimeSlots(Long infosUserId, LocalDate dateDebut, LocalDate dateFin, Pageable pageable);

    @Query("SELECT COUNT(*) FROM historique_connexion entity WHERE entity.actif_yn = :enCoursYN")
    Mono<Long> countAllByEnCoursYN(Boolean enCoursYN);

    @Query("SELECT COUNT(*) FROM historique_connexion entity WHERE (entity.date_debut_connexion BETWEEN :dateDebut AND :dateFin) AND (entity.date_fin_connexion BETWEEN :dateDebut AND :dateFin)")
    Mono<Long> countAllByTimeSlots(LocalDate dateDebut, LocalDate dateFin);

    @Query("SELECT COUNT(*) FROM historique_connexion entity WHERE entity.info_user_id = :infosUserId AND (entity.date_debut_connexion BETWEEN :dateDebut AND :dateFin) AND (entity.date_fin_connexion BETWEEN :dateDebut AND :dateFin)")
    Mono<Long> countAllByInfosUserIdAndTimeSlots(Long infosUserId, LocalDate dateDebut, LocalDate dateFin);
}

interface HistoriqueConnexionRepositoryInternal {
    <S extends HistoriqueConnexion> Mono<S> save(S entity);

    Flux<HistoriqueConnexion> findAllBy(Pageable pageable);

    Flux<HistoriqueConnexion> findAll();

    Mono<HistoriqueConnexion> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<HistoriqueConnexion> findAllBy(Pageable pageable, Criteria criteria);
}
