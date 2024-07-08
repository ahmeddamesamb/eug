package sn.ugb.gateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.repository.InfoUserRessourceRepository;
import sn.ugb.gateway.repository.RessourceRepository;
import sn.ugb.gateway.repository.UserProfileRepository;
import sn.ugb.gateway.repository.search.UserProfileSearchRepository;
import sn.ugb.gateway.service.UserProfileService;
import sn.ugb.gateway.service.dto.InfoUserRessourceDTO;
import sn.ugb.gateway.service.dto.RessourceDTO;
import sn.ugb.gateway.service.dto.UserProfileDTO;
import sn.ugb.gateway.service.mapper.InfoUserRessourceMapper;
import sn.ugb.gateway.service.mapper.RessourceMapper;
import sn.ugb.gateway.service.mapper.UserProfileMapper;
import sn.ugb.gateway.web.rest.errors.BadRequestAlertException;

import java.time.LocalDate;

/**
 * Service Implementation for managing {@link sn.ugb.gateway.domain.UserProfile}.
 */
@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    private final UserProfileRepository userProfileRepository;

    private final UserProfileMapper userProfileMapper;

    private final UserProfileSearchRepository userProfileSearchRepository;


    private static final String ENTITY_NAME = "UserProfile";

    @Autowired
    private RessourceRepository ressourceRepository;

    @Autowired
    private RessourceMapper ressourceMapper;
    @Autowired
    private InfoUserRessourceRepository infoUserRessourceRepository;
    @Autowired
    private InfoUserRessourceMapper infoUserRessourceMapper;


    public UserProfileServiceImpl(
        UserProfileRepository userProfileRepository,
        UserProfileMapper userProfileMapper,
        RessourceRepository ressourceRepository,
        RessourceMapper ressourceMapper,
        UserProfileSearchRepository userProfileSearchRepository
    ) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
        this.ressourceRepository = ressourceRepository;
        this.ressourceMapper = ressourceMapper;
        this.userProfileSearchRepository = userProfileSearchRepository;

    }

    @Override
    public Mono<UserProfileDTO> save(UserProfileDTO userProfileDTO) {
        log.debug("Request to save UserProfile : {}", userProfileDTO);

        validateUserProfile(userProfileDTO, true);

        return userProfileRepository
            .save(userProfileMapper.toEntity(userProfileDTO))
            .flatMap(userProfileSearchRepository::save)
            .map(userProfileMapper::toDto);
    }

    @Override
    public Mono<UserProfileDTO> update(UserProfileDTO userProfileDTO) {
        log.debug("Request to update UserProfile : {}", userProfileDTO);

        validateUserProfile(userProfileDTO, false);

        return userProfileRepository
            .save(userProfileMapper.toEntity(userProfileDTO))
            .flatMap(userProfileSearchRepository::save)
            .map(userProfileMapper::toDto);
    }

    @Override
    public Mono<UserProfileDTO> partialUpdate(UserProfileDTO userProfileDTO) {
        log.debug("Request to partially update UserProfile : {}", userProfileDTO);

        return userProfileRepository
            .findById(userProfileDTO.getId())
            .map(existingUserProfile -> {
                userProfileMapper.partialUpdate(existingUserProfile, userProfileDTO);

                return existingUserProfile;
            })
            .flatMap(userProfileRepository::save)
            .flatMap(savedUserProfile -> {
                userProfileSearchRepository.save(savedUserProfile);
                return Mono.just(savedUserProfile);
            })
            .map(userProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserProfiles");
        return userProfileRepository.findAllBy(pageable).map(userProfileMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userProfileRepository.count();
    }

    public Mono<Long> searchCount() {
        return userProfileSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserProfileDTO> findOne(Long id) {
        log.debug("Request to get UserProfile : {}", id);
        return userProfileRepository.findById(id).map(userProfileMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserProfile : {}", id);
        return userProfileRepository.deleteById(id).then(userProfileSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserProfileDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserProfiles for query {}", query);
        return userProfileSearchRepository.search(query, pageable).map(userProfileMapper::toDto);
    }

    private void validateUserProfile(UserProfileDTO userProfileDTO, boolean isSaveOperation) {
        if (userProfileDTO == null) {
            throw new BadRequestAlertException("UserProfile cannot be null", ENTITY_NAME, "nullUserProfile");
        }

        if (userProfileDTO.getDateDebut() == null) {
            throw new BadRequestAlertException("La date de début est obligatoire", ENTITY_NAME, "dateDebutObligatoire");
        }

        if (userProfileDTO.getEnCoursYN() == null) {
            throw new BadRequestAlertException("Le champ en cours est obligatoire", ENTITY_NAME, "enCoursObligatoire");
        }

        if (!userProfileDTO.getEnCoursYN() && userProfileDTO.getDateFin() == null) {
            throw new BadRequestAlertException("La date de fin doit être renseignée lorsque le profil n'est pas en cours", ENTITY_NAME, "dateFinObligatoire");
        }

        if (userProfileDTO.getProfil() != null && !userProfileDTO.getProfil().getActifYN()) {
            throw new BadRequestAlertException("Seuls les profils actifs peuvent être attribués", ENTITY_NAME, "profilInactif");
        }

        if (!isSaveOperation) {
            userProfileRepository.findById(userProfileDTO.getId())
                .switchIfEmpty(Mono.error(new BadRequestAlertException("UserProfile avec id " + userProfileDTO.getId() + " non trouvé", ENTITY_NAME, "userProfileNotFound")))
                .subscribe();
        }
    }
    @Override
    public Mono<UserProfileDTO> archiveUserProfil(Long id) {
        log.debug("Request to archive UserProfile : {}", id);
        return userProfileRepository.findById(id)
            .flatMap(userProfile -> {
                userProfile.setEnCoursYN(false);
                userProfile.setDateFin(LocalDate.now());
                return userProfileRepository.save(userProfile);
            })
            .flatMap(userProfileSearchRepository::save)
            .map(userProfileMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Flux<UserProfileDTO> getAllUserProfilByProfilId(Long profilId, Pageable pageable) {
        log.debug("Request to get all UserProfiles by ProfilId : {}", profilId);
        return userProfileRepository.findAllByProfilId(profilId, pageable).map(userProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserProfileDTO> getAllUserProfilByInfosUserId(Long infoUserId, Pageable pageable) {
        log.debug("Request to get all UserProfiles by InfosUserId : {}", infoUserId);
        return userProfileRepository.findAllByInfoUserId(infoUserId, pageable).map(userProfileMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Flux<UserProfileDTO> getAllUserProfilByEncoursYN(Boolean enCoursYN, Pageable pageable) {
        log.debug("Request to get all UserProfiles by enCoursYN : {}", enCoursYN);
        return userProfileRepository.findAllByEnCoursYN(enCoursYN, pageable).map(userProfileMapper::toDto);
    }

    @Override
    public Mono<Long> getAllUserProfilByInfosUserIdCount(Long infoUserId) {
        return userProfileRepository.countByInfoUserId(infoUserId);
    }

    @Override
    public Mono<Long> getAllUserProfilByProfilIdCount(Long profilId) {
        return userProfileRepository.countByProfilId(profilId);
    }
    @Override
    public Mono<Long> getAllUserProfilByEncoursYNCount(Boolean enCoursYN) {
        return userProfileRepository.countByEncoursYN(enCoursYN);
    }


    @Override
    public Mono<Long> getAllResourceByInfoUserIdCount(Long infoUserId) {
        return null;
    }

    @Override
    public Flux<InfoUserRessourceDTO> getAllResourceByInfoUserId(Long infoUserId, Pageable pageable) {
        return infoUserRessourceRepository.findAllByInfosUserId(infoUserId, pageable).map(infoUserRessourceMapper::toDto);
    }

    @Override
    public Mono<Long> getAllRessourceByUserProfilIdCount(Long userProfileId) {
        return null;
    }

    @Override
    public Flux<RessourceDTO> getAllRessourceByUserProfilId(Long userProfileId, Pageable pageable) {
        return null;
    }









    // Autres méthodes existantes





   /*
    @Override
    @Transactional(readOnly = true)
    public Flux<RessourceDTO> getAllResourceByInfoUserId(Long infoUserId, Pageable pageable) {
        log.debug("Request to get all Resources by InfoUserId : {}", infoUserId);
        return ressourceRepository.findAllByInfoUserId(infoUserId, pageable).map(ressourceMapper::toDto);
    }

   private void validateUserProfileData(UserProfileDTO userProfileDTO) {
        if (userProfileDTO == null) {
            throw new BadRequestAlertException("UserProfile cannot be null", ENTITY_NAME, "nullUserProfile");
        }

        LocalDate dateDebut = userProfileDTO.getDateDebut();
        LocalDate dateFin = userProfileDTO.getDateFin();
        Boolean enCoursYN = userProfileDTO.getEnCoursYN();
        Long profilId = userProfileDTO.getProfilId();
        Profil profil = userProfileDTO.getProfil();

        // RG1: Tous les champs sont obligatoires sauf la date de fin
        if (dateDebut == null) {
            throw new BadRequestAlertException("La date de début est obligatoire", ENTITY_NAME, "dateDebutObligatoire");
        }
        if (enCoursYN == null) {
            throw new BadRequestAlertException("Le champ en cours est obligatoire", ENTITY_NAME, "enCoursYNObligatoire");
        }

        // RG2: Seuls les profils actifs peuvent être attribués
        if (profil != null && !profil.getActifYN()) {
            throw new BadRequestAlertException("Seuls les profils actifs peuvent être attribués", ENTITY_NAME, "profilInactif");
        }

        // RG3: Si le champ en cours est à 0 alors le profil est retiré à l’utilisateur et la date de fin doit être renseignée
        if (!enCoursYN && dateFin == null) {
            throw new BadRequestAlertException("La date de fin doit être renseignée si le profil n'est plus en cours", ENTITY_NAME, "dateFinObligatoire");
        }
    }
*/
}
