package sn.ugb.gateway.service.mapper;

import org.mapstruct.*;
import sn.ugb.gateway.domain.InfosUser;
import sn.ugb.gateway.domain.Profil;
import sn.ugb.gateway.domain.UserProfile;
import sn.ugb.gateway.service.dto.InfosUserDTO;
import sn.ugb.gateway.service.dto.ProfilDTO;
import sn.ugb.gateway.service.dto.UserProfileDTO;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfilMapper.class,InfosUserMapper.class})
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {
    @Mapping(target = "profil", source = "profil", qualifiedByName = "profilId")
    @Mapping(target = "infoUser", source = "infoUser", qualifiedByName = "infosUserId")
    UserProfileDTO toDto(UserProfile s);

    @Named("profilId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    @Mapping(target = "dateAjout", source = "dateAjout")
    @Mapping(target = "actifYN", source = "actifYN")
    ProfilDTO toDtoProfilId(Profil profil);

    @Named("infosUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dateAjout", source = "dateAjout")
    @Mapping(target = "actifYN", source = "actifYN")
    InfosUserDTO toDtoInfosUserId(InfosUser infosUser);


}
