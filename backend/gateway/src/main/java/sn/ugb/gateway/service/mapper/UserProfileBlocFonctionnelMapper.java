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
@Mapper(componentModel = "spring", uses = {UserProfileMapper.class, UserProfileBlocFonctionnelMapper.class})
public interface UserProfileBlocFonctionnelMapper extends EntityMapper<UserProfileBlocFonctionnelDTO, UserProfileBlocFonctionnel> {
    @Mapping(target = "userProfil", source = "userProfil")
    @Mapping(target = "blocFonctionnel", source = "blocFonctionnel")
    UserProfileBlocFonctionnelDTO toDto(UserProfileBlocFonctionnel s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("blocFonctionnelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BlocFonctionnelDTO toDtoBlocFonctionnelId(BlocFonctionnel blocFonctionnel);
}
