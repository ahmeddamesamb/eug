package sn.ugb.gateway.service.mapper;

import org.mapstruct.*;
import sn.ugb.gateway.domain.InfoUserRessource;
import sn.ugb.gateway.domain.InfosUser;
import sn.ugb.gateway.domain.Ressource;
import sn.ugb.gateway.service.dto.InfoUserRessourceDTO;
import sn.ugb.gateway.service.dto.InfosUserDTO;
import sn.ugb.gateway.service.dto.RessourceDTO;

/**
 * Mapper for the entity {@link InfoUserRessource} and its DTO {@link InfoUserRessourceDTO}.
 */
@Mapper(componentModel = "spring", uses = {InfosUserMapper.class, RessourceMapper.class})
public interface InfoUserRessourceMapper extends EntityMapper<InfoUserRessourceDTO, InfoUserRessource> {
    @Mapping(target = "infosUser", source = "infosUser", qualifiedByName = "infosUserId")
    @Mapping(target = "ressource", source = "ressource", qualifiedByName = "ressourceId")
    InfoUserRessourceDTO toDto(InfoUserRessource s);

    @Named("infosUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dateAjout", source = "dateAjout")
    @Mapping(target = "actifYN", source = "actifYN")
    InfosUserDTO toDtoInfosUserId(InfosUser infosUser);

    @Named("ressourceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelle", source = "libelle")
    @Mapping(target = "actifYN", source = "actifYN")
    RessourceDTO toDtoRessourceId(Ressource ressource);
}
