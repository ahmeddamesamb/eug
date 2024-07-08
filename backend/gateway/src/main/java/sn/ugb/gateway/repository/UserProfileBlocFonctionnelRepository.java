package sn.ugb.gateway.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.domain.ServiceUser;
import sn.ugb.gateway.domain.UserProfileBlocFonctionnel;

/**
 * Spring Data R2DBC repository for the UserProfileBlocFonctionnel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProfileBlocFonctionnelRepository
    extends ReactiveCrudRepository<UserProfileBlocFonctionnel, Long>, UserProfileBlocFonctionnelRepositoryInternal {
    Flux<UserProfileBlocFonctionnel> findAllBy(Pageable pageable);

    @Query("SELECT * FROM user_profile_bloc_fonctionnel entity WHERE entity.user_profil_id = :id")
    Flux<UserProfileBlocFonctionnel> findByUserProfil(Long id);

    @Query("SELECT * FROM user_profile_bloc_fonctionnel entity WHERE entity.user_profil_id IS NULL")
    Flux<UserProfileBlocFonctionnel> findAllWhereUserProfilIsNull();

    @Query("SELECT * FROM user_profile_bloc_fonctionnel entity WHERE entity.bloc_fonctionnel_id = :id")
    Flux<UserProfileBlocFonctionnel> findByBlocFonctionnel(Long id);

    @Query("SELECT * FROM user_profile_bloc_fonctionnel entity WHERE entity.bloc_fonctionnel_id IS NULL")
    Flux<UserProfileBlocFonctionnel> findAllWhereBlocFonctionnelIsNull();

    @Override
    <S extends UserProfileBlocFonctionnel> Mono<S> save(S entity);

    @Override
    Flux<UserProfileBlocFonctionnel> findAll();

    @Override
    Mono<UserProfileBlocFonctionnel> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);

    @Query("SELECT * FROM user_profile_bloc_fonctionnel WHERE user_profil_id = :userProfileId")
    Flux<UserProfileBlocFonctionnel> findAllByUserProfileId(Long userProfileId, Pageable pageable);

    @Query("SELECT COUNT(*) FROM user_profile_bloc_fonctionnel WHERE user_profil_id = :userProfileId")
    Mono<Long> countByUserProfileId(Long userProfileId);

    @Query("SELECT * FROM user_profile_bloc_fonctionnel WHERE bloc_fonctionnel_id = :blocFonctionnelId")
    Flux<UserProfileBlocFonctionnel> findByBlocFonctionnelId(Long blocFonctionnelId, Pageable pageable);

    @Query("SELECT COUNT(*) FROM user_profile_bloc_fonctionnel WHERE bloc_fonctionnel_id = :blocFonctionnelId")
    Mono<Long> countByBlocFonctionnelId(Long blocFonctionnelId);

    @Query("SELECT * FROM user_profile_bloc_fonctionnel WHERE en_cours_yn = :enCoursYN")
    Flux<UserProfileBlocFonctionnel> findByEnCoursYN(Boolean enCoursYN, Pageable pageable);

    @Query("SELECT COUNT(*) FROM user_profile_bloc_fonctionnel WHERE en_cours_yn = :enCoursYN")
    Mono<Long> countByEnCoursYN(Boolean enCoursYN);

    @Query("""
            SELECT s.* FROM service_user s
            JOIN bloc_fonctionnel bf ON s.id = bf.service_id
            JOIN user_profile_bloc_fonctionnel upbf ON bf.id = upbf.bloc_fonctionnel_id
            WHERE upbf.user_profil_id = :userProfileId
        """)
    Flux<ServiceUser> findAllServiceByUserProfileId(Long userProfileId, Pageable pageable);
}

interface UserProfileBlocFonctionnelRepositoryInternal {
    <S extends UserProfileBlocFonctionnel> Mono<S> save(S entity);

    Flux<UserProfileBlocFonctionnel> findAllBy(Pageable pageable);

    Flux<UserProfileBlocFonctionnel> findAll();

    Mono<UserProfileBlocFonctionnel> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserProfileBlocFonctionnel> findAllBy(Pageable pageable, Criteria criteria);
}
