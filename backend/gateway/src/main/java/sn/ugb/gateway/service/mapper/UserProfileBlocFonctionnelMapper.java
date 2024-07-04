package sn.ugb.gateway.service.mapper;

import org.mapstruct.*;
import sn.ugb.gateway.domain.BlocFonctionnel;
import sn.ugb.gateway.domain.UserProfile;
import sn.ugb.gateway.domain.UserProfileBlocFonctionnel;
import sn.ugb.gateway.service.dto.BlocFonctionnelDTO;
import sn.ugb.gateway.service.dto.UserProfileBlocFonctionnelDTO;
import sn.ugb.gateway.service.dto.UserProfileDTO;

/**
 * Mapper for the entity {@link UserProfileBlocFonctionnel} and its DTO {@link UserProfileBlocFonctionnelDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfilMapper.class, InfosUserMapper.class, ServiceUserMapper.class})
public interface UserProfileBlocFonctionnelMapper extends EntityMapper<UserProfileBlocFonctionnelDTO, UserProfileBlocFonctionnel> {
    @Mapping(target = "userProfil", source = "userProfil", qualifiedByName = "userProfileId")
    @Mapping(target = "blocFonctionnel", source = "blocFonctionnel", qualifiedByName = "blocFonctionnelId")
    UserProfileBlocFonctionnelDTO toDto(UserProfileBlocFonctionnel s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dateDebut", source = "dateDebut")
    @Mapping(target = "dateFin", source = "dateFin")
    @Mapping(target = "enCoursYN", source = "enCoursYN")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("blocFonctionnelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleBloc", source = "libelleBloc")
    @Mapping(target = "dateAjoutBloc", source = "dateAjoutBloc")
    @Mapping(target = "actifYN", source = "actifYN")
    BlocFonctionnelDTO toDtoBlocFonctionnelId(BlocFonctionnel blocFonctionnel);
}
